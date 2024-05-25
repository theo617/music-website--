<template>
  <el-container class="singer-detail">
    <el-aside class="album-slide">
      <el-image class="singer-img" fit="contain" :src="attachImageUrl(songDetails.pic)" />
      <div class="album-info">
        <h2>基本资料</h2>
        <ul>
          <li v-if="songDetails.sex !== 2">性别：{{ getUserSex(songDetails.sex) }}</li>
          <li>生日：{{ getBirth(songDetails.birth) }}</li>
          <li>故乡：{{ songDetails.location }}</li>
        </ul>
      </div>
    </el-aside>
    <el-main class="album-main">
      <h1>{{ songDetails.name }}</h1>
      <p>{{ songDetails.introduction }}</p>
      <song-list :songList="currentSongList"></song-list>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import SongList from "@/components/SongList.vue";
import { HttpManager } from "@/api";
import { getBirth } from "@/utils";

export default defineComponent({
  components: {
    SongList,
  },
  setup() {
    const store = useStore();
    const { getUserSex } = mixin();

    const currentSongList = ref([]);
    const songDetails = computed(() => store.getters.songDetails) as any;

    onMounted(async () => {
      try {
        const result = (await HttpManager.getSongOfSingerId(songDetails.value.id)) as ResponseBody;
        currentSongList.value = result.data;
      } catch (error) {
        console.error(error);
      }
    });

    return {
      songDetails,
      currentSongList,
      attachImageUrl: HttpManager.attachImageUrl,
      getBirth,
      getUserSex,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.singer-detail {
  display: flex;
  min-height: 150vh;
  background: linear-gradient(to right, #ffecd2 0%, #fcb69f 100%);
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
}

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

  .singer-img {
    height: 300px;
    width: 300px;
    border-radius: 50%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }

  .album-info {
    width: 80%;
    padding-top: 2rem;
    ul {
      list-style: none;
      padding: 0;
      li {
        width: 100%;
        height: 30px;
        line-height: 30px;
        font-size: 1.1rem;
      }
    }
  }
}

.album-main {
  flex: 1;
  margin-left: 40px;
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

  h1 {
    font-size: 2rem;
    margin-bottom: 10px;
  }

  p {
    color: rgba(0, 0, 0, 0.7);
    margin-bottom: 20px;
  }
}
</style>
