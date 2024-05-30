<template>
    <div class="user-song">
      <div class="header">
        <h2>{{ userInfo.username }} 的原创歌曲</h2>
      </div>
      <div v-if="currentSongList.length > 0" class="song-list">
        <song-list :songList="currentSongList"></song-list>
      </div>
      <div v-else class="no-data">暂无原创歌曲</div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, reactive, computed, onMounted } from "vue";
  import { useRoute } from "vue-router";
  import { HttpManager } from "@/api";
  import SongList from "@/components/SongList.vue";
  
  export default defineComponent({
    components: {
      SongList,
    },
    setup() {
      const route = useRoute();
      const userId = route.params.id;
      const userInfo = reactive({
        username: "",
        sex: "",
        birth: "",
        location: "",
        introduction: "",
      });
      const currentSongList = ref([]);
  
      const fetchUserInfo = async (id) => {
        const result = await (HttpManager.getUserOfId(id)) as ResponseBody;
        if (result.success && result.data.length > 0) {
          const user = result.data[0];
          userInfo.username = user.username;
          userInfo.sex = user.sex;
          userInfo.birth = user.birth;
          userInfo.location = user.location;
          userInfo.introduction = user.introduction;
  
          // 查找歌手ID
          fetchSingerSongs(user.username);
        }
      };
  
      const fetchSingerSongs = async (username) => {
        const result = await (HttpManager.getAllSinger()) as ResponseBody;
        if (result.success && result.data.length > 0) {
          const singer = result.data.find(singer => singer.name === username);
          if (singer) {
            fetchSongsOfSinger(singer.id);
          }
        }
      };
  
      const fetchSongsOfSinger = async (singerId) => {
        const result = await (HttpManager.getSongOfSingerId(singerId)) as ResponseBody;
        if (result.success) {
          currentSongList.value = result.data;
        }
      };
  
      onMounted(() => {
        fetchUserInfo(userId);
      });
  
      return {
        userInfo,
        currentSongList,
      };
    },
  });
  </script>
  
  <style scoped>
  .user-song {
    padding: 20px;
  
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }
  
    .no-data {
      text-align: center;
      color: grey;
      margin-top: 20px;
    }
  
    .song-list {
      margin-top: 20px;
    }
  }
  </style>
  