<template>
  <el-container>
    <el-aside class="album-slide">
      <el-image class="album-img" fit="contain" :src="attachImageUrl(songDetails.pic)" />
      <h3 class="album-info">{{ songDetails.title }}</h3>
    </el-aside>
    <el-main class="album-main">
      <div class="album-header">
        <h1>{{ songDetails.title }}</h1>
        <p>{{ songDetails.introduction }}</p>
      </div>
      <!--评分-->
      <div class="album-score">
        <div class="score-item">
          <h3>歌单评分</h3>
          <el-rate v-model="rank" allow-half disabled></el-rate>
          <span>{{ rank * 2 }}</span>
        </div>
        <div class="score-item">
          <h3>{{ assistText }}</h3>
          <el-rate allow-half v-model="score" :disabled="disabledRank" @change="pushValue"></el-rate>
          <span>{{ score * 2 }}</span>
        </div>
      </div>
      <!--歌曲-->
      <song-list class="album-body" :songList="currentSongList"></song-list>
      <comment :playId="songListId" :type="1"></comment>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { defineComponent, ref, computed, getCurrentInstance } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import SongList from "@/components/SongList.vue";
import Comment from "@/components/Comment.vue";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    SongList,
    Comment,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { checkStatus } = mixin();

    const currentSongList = ref([]); // 存放的音乐
    const nowSongListId = ref(""); // 歌单 ID
    const nowScore = ref(0);
    const nowRank = ref(0);
    const disabledRank = ref(false);
    const assistText = ref("评价");
    const songDetails = computed(() => store.getters.songDetails); // 单个歌单信息
    const nowUserId = computed(() => store.getters.userId);
  
    nowSongListId.value = songDetails.value.id; // 给歌单ID赋值
  
    // 收集歌单里面的歌曲
    async function getSongId(id) {
      const result = (await HttpManager.getListSongOfSongId(id)) as ResponseBody;
      // 获取歌单里的歌曲信息
      for (const item of result.data) {
        // 获取单里的歌曲
        const resultSong = (await HttpManager.getSongOfId(item.songId)) as ResponseBody;
        currentSongList.value.push(resultSong.data[0]);
      }
    }
    // 获取评分
    async function getRank(id) {
      const result = (await HttpManager.getRankOfSongListId(id)) as ResponseBody;
      nowRank.value = result.data / 2;
    }
    async function getUserRank(userId, songListId) {
      const result = (await HttpManager.getUserRank(userId, songListId)) as ResponseBody;
      nowScore.value = result.data / 2;
      disabledRank.value = true;
      assistText.value = "已评价";
    }
    // 提交评分
    async function pushValue() {
      if (disabledRank.value || !checkStatus()) return;

      const songListId = nowSongListId.value;
      var consumerId = nowUserId.value;
      const score = nowScore.value * 2;
      try {
        const result = (await HttpManager.setRank({ songListId, consumerId, score })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });

        if (result.success) {
          getRank(nowSongListId.value);
          disabledRank.value = true;
          assistText.value = "已评价";
        }
      } catch (error) {
        console.error(error);
      }
    }

    getUserRank(nowUserId.value, nowSongListId.value);
    getRank(nowSongListId.value); // 获取评分
    getSongId(nowSongListId.value); // 获取歌单里面的歌曲ID

    return {
      songDetails,
      rank: nowRank,
      score: nowScore,
      disabledRank,
      assistText,
      currentSongList,
      songListId: nowSongListId,
      attachImageUrl: HttpManager.attachImageUrl,
      pushValue,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-right: 1px solid #e0e0e0;

  .album-img {
    height: 250px;
    width: 250px;
    border-radius: 10%;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .album-info {
    width: 70%;
    padding-top: 2rem;
    text-align: center;
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
  }
}

.album-main {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .album-header {
    margin-bottom: 20px;

    h1 {
      font-size: 2rem;
      font-weight: bold;
      color: #333;
      margin: 0;
    }

    p {
      color: rgba(0, 0, 0, 0.6);
      margin: 10px 0 20px 0px;
      line-height: 1.6;
    }
  }

  .album-score {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .score-item {
      display: flex;
      align-items: center;

      h3 {
        margin-right: 10px;
        font-size: 1rem;
        color: #333;
      }

      span {
        font-size: 1.5rem;
        font-weight: bold;
        color: #ff6600;
        margin-left: 10px;
      }
    }
  }

  .album-body {
    margin: 20px 0;
  }
}

@media screen and (min-width: $sm) {
  .album-slide {
    width: 350px;
  }
  .album-main {
    margin-left: 200px;
  }
}

@media screen and (max-width: $sm) {
  .album-slide {
    display: none;
  }
  .album-main {
    padding: 10px;
  }
}
</style>
