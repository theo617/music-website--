<template>
  <div class="lyric-page" :class="backgroundClass">
    <div class="song-info">
      <h2>{{ songTitle }}</h2>
      <p>{{ singerName }}</p>
    </div>
    <div class="lyric-content">
      <div class="left">
        <div class="record-container">
          <div :class="['record', { spinning: isPlay }]" :style="{ transform: recordRotation }">
            <div class="record-center">
              <img class="record-img" :src="attachImageUrl(songPic)" />
            </div>
          </div>
          <div :class="['needle', { plaTsyg: isPlay }]"></div>
        </div>
      </div>
      <div class="right">
        <div class="lyrics" ref="lyricsContainer">
          <div class="lyric-line" v-for="(line, index) in lyricArr" :key="index" :class="{ active: isActive(index) }">
            {{ line[1] }}
          </div>
        </div>
      </div>
    </div>
    <comment :playId="songId" :type="0"></comment>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, watch, onMounted } from "vue";
import { useStore } from "vuex";
import Comment from "@/components/Comment.vue";
import { parseLyric } from "@/utils";
import { HttpManager } from "@/api";

const backgroundClasses = ['style1', 'style2', 'style3', 'style4'];

export default defineComponent({
  components: {
    Comment,
  },
  setup() {
    const store = useStore();

    const lyricArr = ref([]); // 当前歌曲的歌词
    const songId = computed(() => store.getters.songId); // 歌曲ID
    const lyric = computed(() => store.getters.lyric); // 歌词
    const currentPlayList = computed(() => store.getters.currentPlayList); // 存放的音乐
    const currentPlaTsydex = computed(() => store.getters.currentPlaTsydex); // 当前歌曲在歌曲列表的位置
    const curTime = computed(() => store.getters.curTime);
    const songTitle = computed(() => store.getters.songTitle); // 歌名
    const singerName = computed(() => store.getters.singerName); // 歌手名
    const songPic = computed(() => store.getters.songPic); // 歌曲图片
    const isPlay = computed(() => store.getters.isPlay); // 播放状态
    const backgroundClass = ref(backgroundClasses[Math.floor(Math.random() * backgroundClasses.length)]);

    const lyricsContainer = ref<HTMLElement | null>(null);
    const rotation = ref(0);

    const updateLyrics = () => {
      if (lyricsContainer.value && lyricArr.value.length) {
        let activeIndex = 0;
        for (let i = 0; i < lyricArr.value.length; i++) {
          if (curTime.value >= lyricArr.value[i][0]) {
            activeIndex = i;
          }
        }
        const offset = Math.max(0, activeIndex - 4);
        lyricsContainer.value.scrollTop = offset * 40;
      }
    };

    const rotateRecord = () => {
      if (isPlay.value) {
        rotation.value += 0.1; // 调整旋转速度
      }
      requestAnimationFrame(rotateRecord);
    };

    watch(songId, () => {
      lyricArr.value = parseLyric(currentPlayList.value[currentPlaTsydex.value].lyric);
      updateLyrics();
    });

    watch(curTime, updateLyrics);

    onMounted(() => {
      lyricArr.value = lyric.value ? parseLyric(lyric.value) : [];
      requestAnimationFrame(rotateRecord);
    });

    const isActive = (index: number) => {
      if (!lyricArr.value.length) return false;
      const currentTime = curTime.value;
      return (
        (index === lyricArr.value.length - 1 && currentTime >= lyricArr.value[index][0]) ||
        (index < lyricArr.value.length - 1 && currentTime >= lyricArr.value[index][0] && currentTime < lyricArr.value[index + 1][0])
      );
    };

    const recordRotation = computed(() => `rotate(${rotation.value}deg)`);

    return {
      songPic,
      singerName,
      songTitle,
      lyricArr,
      songId,
      isPlay,
      attachImageUrl: HttpManager.attachImageUrl,
      lyricsContainer,
      isActive,
      backgroundClass,
      recordRotation,
    };
  },
});
</script>

<style lang="scss" scoped>
$background-styles: (
  "style1": radial-gradient(circle, rgba(68, 68, 68, 1) 0%, rgba(34, 34, 34, 1) 100%),
  "style2": linear-gradient(135deg, rgba(255, 105, 180, 1) 0%, rgba(255, 182, 193, 1) 100%),
  "style3": radial-gradient(circle, rgba(2, 0, 36, 1) 0%, rgba(9, 9, 121, 1) 35%, rgba(0, 212, 255, 1) 100%),
  "style4": linear-gradient(120deg, rgba(252, 70, 107, 1) 0%, rgba(63, 94, 251, 1) 100%),
);

.lyric-page {
  color: #fff;
  height: 100vh;
  overflow-y: auto;
  padding: 20px;

  @each $style-name, $style-value in $background-styles {
    &.#{$style-name} {
      background: $style-value;
    }
  }

  .song-info {
    text-align: center;
    margin-bottom: 20px;
    h2 {
      font-size: 24px;
      margin-bottom: 10px;
    }
    p {
      font-size: 18px;
    }
  }

  .lyric-content {
    display: flex;
    justify-content: center;
    align-items: center;

    .left {
      position: relative;
      .record-container {
        position: relative;
        width: 450px; /* 调整唱片尺寸 */
        height: 450px;
        margin-right: 20px;

        .record {
          width: 100%;
          height: 100%;
          border-radius: 50%;
          background: radial-gradient(circle, #222 20%, #000 100%);
          box-shadow: inset 0 0 10px #000, 0 0 20px rgba(0, 0, 0, 0.5); /* 增加阴影 */
          position: relative;
          z-index: 1;
          background-image: radial-gradient(circle, #000 1px, transparent 1px),
                            radial-gradient(circle, #000 1px, transparent 1px);
          background-size: 20px 20px;
          background-position: 0 0, 10px 10px;
        }

        .record-center {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          width: 60%;
          height: 60%;
          border-radius: 50%;
          background: #fff;
        }

        .record-img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }

        .needle {
          width: 150px;
          height: 20px;
          background: transparent;
          position: absolute;
          top: -20px;
          right: -20px;
          transform: rotate(35deg);
          transform-origin: top right;
          transition: transform 0.3s ease;
          &.plaTsyg {
            transform: rotate(5deg);
          }
          &::before {
            content: '';
            width: 10px;
            height: 50px;
            border-radius: 20%;
            background: #fff;
            position: absolute;
            top: -1px;
            left: -5px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
          }
          &::after {
            content: '';
            width: 140px;
            height: 10px;
            background: #fff;
            position: absolute;
            top: 0;
            left: 0;
            border-radius: 5px;
          }
        }
      }
    }

    .right {
      flex: 1;
      .lyrics {
        max-height: 500px;
        overflow-y: auto;
        .lyric-line {
          font-size: 16px;
          line-height: 40px;
          text-align: center;
          transition: all 0.3s;
          opacity: 0.5;
        }
        .lyric-line.active {
          font-size: 20px;
          color: #fff;
          opacity: 1;
        }
      }
    }
  }

  .comment {
    margin-top: 20px;
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
