<template>
    <div class="user-song-list">
      <div class="header">
        <h2>{{ username }}的歌单</h2>
      </div>
      <div v-if="songLists.length > 0" class="song-list">
        <play-list :playList="data" path="personal-song-sheet-detail"></play-list> 
      </div>
      <div v-else class="no-data">暂无歌单</div>
      <el-pagination
        class="pagination"
        background
        layout="total, prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="songLists.length"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, onMounted, computed, reactive } from "vue";
  import { useRoute } from "vue-router";
  import { HttpManager } from "@/api";
  import PlayList from "@/components/PlayList.vue";
  
  export default defineComponent({
    components: {
      PlayList,
    },
    setup() {
      const route = useRoute();
      const songLists = ref([]);
      const userId = computed(() => route.params.id);
      const username = ref('');
      const pageSize = ref(15); // 页数
      const currentPage = ref(1); // 当前页
      const data = computed(() => songLists.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value));
  
      async function getsongListConsumerOfUserId(user_id) {
        songLists.value = ((await HttpManager.songListConsumerOfUserId(user_id)) as ResponseBody).data;
        currentPage.value = 1;
      }
  
      async function getUserInfo(id) {
        const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
        if (result.success && result.data.length > 0) {
          username.value = result.data[0].username;
        }
      }
  
      onMounted(() => {
        getsongListConsumerOfUserId(userId.value);
        getUserInfo(userId.value);
      });
  
      // 获取当前页
      function handleCurrentChange(val) {
        currentPage.value = val;
      }
  
      return {
        songLists,
        data,
        handleCurrentChange,
        pageSize,
        currentPage,
        username,
      };
    },
  });
  </script>
  
  <style scoped>
  .user-song-list {
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
  }
  </style>
  