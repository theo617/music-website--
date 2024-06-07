<template>
  <div class="play-bar" :class="{ show: !toggle }">
    <div class="fold" :class="{ turn: toggle }">
      <Tsy-icon :icon="iconList.ZHEDIE" @click="toggle = !toggle"></Tsy-icon>
    </div>
    <!--播放进度-->
    <el-slider class="progress" v-model="startTime" @change="changeTime" size="small"></el-slider>
    <div class="control-box">
      <div class="info-box">
        <!--歌曲图片-->
        <div @click="goPlayerPage">
          <el-image class="song-bar-img" fit="contain" :src="attachImageUrl(songPic)"/>
        </div>
        <!--播放开始结束时间-->
        <div v-if="songId">
          <div class="song-info">{{ this.songTitle }} - {{ this.singerName }}</div>
          <div class="time-info">{{ startTime }} / {{ endTime }}</div>
        </div>
      </div>
      <div class="song-ctr">
        <Tsy-icon class="Tsy-play-show" :icon="playStateList[playStateIndex]" @click="changePlayState"></Tsy-icon>
        <!--上一首-->
        <Tsy-icon class="Tsy-play-show" :icon="iconList.SHANGYISHOU" @click="prev" style="color: white !important;"></Tsy-icon>
        <!--播放-->
        <Tsy-icon :icon="playBtnIcon" @click="togglePlay" ></Tsy-icon>
        <!--下一首-->
        <Tsy-icon class="Tsy-play-show" :icon="iconList.XIAYISHOU" @click="next"></Tsy-icon>
        <!--音量-->
        <el-dropdown class="Tsy-play-show" trigger="click">
          <Tsy-icon v-if="volume !== 0" :icon="iconList.TsyLIANG" ></Tsy-icon>
          <Tsy-icon v-else :icon="iconList.JINGTsy" style="color: white;"></Tsy-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-slider class="Tsy-slider" style="height: 150px; margin: 10px 0" v-model="volume"
                         :vertical="true"></el-slider>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="song-ctr song-edit">
        <!--收藏到喜欢歌曲-->
        <Tsy-icon
            class="Tsy-play-show"
            :class="{ active: isCollection }"
            :icon="isCollection ? iconList.like : iconList.dislike"
            @click="changeCollection"
        ></Tsy-icon>
        <!--下载-->
        <Tsy-icon
            class="Tsy-play-show"
            :icon="iconList.download"
            @click="
            downloadMusic({
              songUrl,
              songName: singerName + '-' + songTitle,
            })
          "
        ></Tsy-icon>
        <!--歌曲列表-->
        <Tsy-icon :icon="iconList.LIEBIAO" @click="changeAside"></Tsy-icon>
        <!--播放记录-->
        <Clock v-if="checkStatus" style="width:1.3em;height:1.3em; margin-right: 5px; fill:#fff " @click="togglePlayHistory" />
      </div>
    </div>
    <PlayHistory :visible="playHistoryVisible" @update:visible="playHistoryVisible = $event" />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, getCurrentInstance, onMounted, ref, watch } from "vue";
import { mapGetters, useStore } from "vuex";
import mixin from "@/mixins/mixin";
import TsyIcon from "./TsyIcon.vue";
import { HttpManager } from "@/api";
import { formatSeconds } from "@/utils";
import { Icon, RouterName } from "@/enums";
import { Clock } from '@element-plus/icons-vue';
import PlayHistory from "@/components/layouts/PlayHistory.vue";

export default defineComponent({
  components: {
    TsyIcon,
    Clock,
    PlayHistory
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { routerManager, playMusic, checkStatus, downloadMusic } = mixin();

    const isCollection = ref(false); // 是否收藏
    const playHistoryVisible = ref(false); // 控制播放记录悬浮框的显示

    const userIdVO = computed(() => store.getters.userId);
    const songIdVO = computed(() => store.getters.songId);
    const token = computed(() => store.getters.token);

    watch(songIdVO, () => {
      initCollection();
    });
    watch(token, (value) => {
      if (!value) isCollection.value = false;
    });

    async function initCollection() {
      if (!checkStatus(false)) return;

      const userId = userIdVO.value;
      const type = '0';
      const songId = songIdVO.value;
      isCollection.value = ((await HttpManager.isCollection({ userId, type, songId })) as ResponseBody).data;
    }

    async function changeCollection() {
      if (!checkStatus()) return;

      const userId = userIdVO.value;
      const type = '0'; //这里要看看 不能直接写死
      const songId = songIdVO.value;

      const result = isCollection.value
        ? ((await HttpManager.deleteCollection(userIdVO.value, songIdVO.value)) as ResponseBody)
        : ((await HttpManager.setCollection({ userId, type, songId })) as ResponseBody);
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.data == true || result.data == false) isCollection.value = result.data;
    }

    onMounted(() => {
      if (songIdVO.value) initCollection();
    });

    const togglePlayHistory = () => {

      playHistoryVisible.value = !playHistoryVisible.value;
    };

    return {
      isCollection,
      playMusic,
      routerManager,
      checkStatus,
      attachImageUrl: HttpManager.attachImageUrl,
      changeCollection,
      downloadMusic,
      togglePlayHistory,
      playHistoryVisible,
    };
  },
  data() {
    return {
      startTime: "00:00",
      endTime: "00:00",
      nowTime: 0, // 进度条的位置
      toggle: true,
      volume: 50,
      playState: Icon.XUNHUAN,
      playStateList: [Icon.XUNHUAN, Icon.LUANXU],
      playStateIndex: 0,
      iconList: {
        download: Icon.XIAZAI,
        ZHEDIE: Icon.ZHEDIE,
        SHANGYISHOU: Icon.SHANGYISHOU,
        XIAYISHOU: Icon.XIAYISHOU,
        TsyLIANG: Icon.TsyLIANG1,
        JINGTsy: Icon.JINGTsy,
        LIEBIAO: Icon.LIEBIAO,
        dislike: Icon.Dislike,
        like: Icon.Like,
        clock: Clock
      },
    };
  },
  computed: {
    ...mapGetters([
      "userId",
      "isPlay", // 播放状态
      "playBtnIcon", // 播放状态的图标
      "songId", // 音乐id
      "songUrl", // 音乐地址
      "songTitle", // 歌名
      "singerName", // 歌手名
      "songPic", // 歌曲图片
      "curTime", // 当前音乐的播放位置
      "duration", // 音乐时长
      "currentPlayList",
      "currentPlaTsydex", // 当前歌曲在歌曲列表的位置
      "showAside", // 是否显示侧边栏
      "autoNext", // 用于触发自动播放下一首
    ]),
  },
  created() {
    this.$store.commit("setPlayBtnIcon", Icon.BOFANG); // 初始化播放状态图标为播放图标
    //this.$store.commit("setIsPlay", false);// 其他的初始化逻辑
  },
  watch: {
    // 切换播放状态的图标
    isPlay(value) {
      this.$store.commit("setPlayBtnIcon", value ? Icon.ZANTING : Icon.BOFANG);
    },
    volume() {
      this.$store.commit("setVolume", this.volume / 100);
    },
    // 播放时间的开始和结束
    curTime() {
      this.startTime = formatSeconds(this.curTime);
      this.endTime = formatSeconds(this.duration);
      // 移动进度条
      this.nowTime = (this.curTime / this.duration) * 100;
    },
    // 自动播放下一首
    autoNext() {
      this.next();
    },
  },
  methods: {
    changeAside() {
      this.$store.commit("setShowAside", !this.showAside);
    },
    // 控制音乐播放 / 暂停
    togglePlay() {
      this.$store.commit("setIsPlay", this.isPlay ? false : true);
      
    },
    changeTime() {
      this.$store.commit("setChangeTime", this.duration * (this.nowTime * 0.01));
    },
    changePlayState() {
      this.playStateIndex = this.playStateIndex >= this.playStateList.length - 1 ? 0 : ++this.playStateIndex;
      this.playState = this.playStateList[this.playStateIndex];
    },
    // 上一首
    prev() {
      if (this.playState === Icon.LUANXU) {
        let plaTsydex = Math.floor(Math.random() * this.currentPlayList.length);
        plaTsydex = plaTsydex === this.currentPlaTsydex ? plaTsydex + 1 : plaTsydex;
        this.$store.commit("setCurrentPlaTsydex", plaTsydex);
        this.toPlay(this.currentPlayList[plaTsydex].url);
      } else if (this.currentPlaTsydex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlaTsydex > 0) {
          this.$store.commit("setCurrentPlaTsydex", this.currentPlaTsydex - 1);
          this.toPlay(this.currentPlayList[this.currentPlaTsydex].url);
        } else {
          this.$store.commit("setCurrentPlaTsydex", this.currentPlayList.length - 1);
          this.toPlay(this.currentPlayList[this.currentPlaTsydex].url);
        }
      }
    },
    // 下一首
    next() {
      if (this.playState === Icon.LUANXU) {
        let plaTsydex = Math.floor(Math.random() * this.currentPlayList.length);
        plaTsydex = plaTsydex === this.currentPlaTsydex ? plaTsydex + 1 : plaTsydex;
        this.$store.commit("setCurrentPlaTsydex", plaTsydex);
        this.toPlay(this.currentPlayList[plaTsydex].url);
      } else if (this.currentPlaTsydex !== -1 && this.currentPlayList.length > 1) {
        if (this.currentPlaTsydex < this.currentPlayList.length - 1) {
          this.$store.commit("setCurrentPlaTsydex", this.currentPlaTsydex + 1);
          this.toPlay(this.currentPlayList[this.currentPlaTsydex].url);
        } else {
          this.$store.commit("setCurrentPlaTsydex", 0);
          this.toPlay(this.currentPlayList[0].url);
        }
      }
    },
    // 选中播放
    async toPlay(url: any) {
      if (url && url !== this.songUrl) {
        const song = this.currentPlayList[this.currentPlaTsydex];
        await this.playMusic({
          id: song.id,
          url: url,
          pic: song.pic,
          index: this.currentPlaTsydex,
          name: song.name,
          lyric: song.lyric,
          currentSongList: this.currentPlayList,
        }
      );
      if(this.isPlay==true){
        this.togglePlay();
      }
      }
    },
    goPlayerPage() {
      this.routerManager(RouterName.Lyric, {path: `${RouterName.Lyric}/${this.songId}`});
    },
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/Tsy-play-bar.scss";
</style>


<style lang="scss" scoped>
@import "@/assets/css/Tsy-play-bar.scss";

</style>
