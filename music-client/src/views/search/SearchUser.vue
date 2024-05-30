<template>
    <div class="search-user">
      <user-list :userList="currentUserList"></user-list>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, computed, watch, onMounted, getCurrentInstance } from "vue";
  import { useStore } from "vuex";
  import UserList from "@/components/UserList.vue";
  import { HttpManager } from "@/api";
  
  export default defineComponent({
    components: {
      UserList,
    },
    setup() {
      const { proxy } = getCurrentInstance();
      const store = useStore();
  
      const currentUserList = ref([]); // 存放的用户
      const searchWord = computed(() => store.getters.searchWord);
      watch(searchWord, (value) => {
        searchUser(value);
      });
  
      // 搜索用户
      async function searchUser(value) {
        if (!value) {
          currentUserList.value = [];
          return;
        }
        const result = (await HttpManager.getUserLikeUsername(value)) as ResponseBody;
        if (!result.data || !result.data.length) {
          currentUserList.value = [];
          (proxy as any).$message({
            message: "暂时没有相关用户",
            type: "warning",
          });
        } else {
          currentUserList.value = result.data;
        }
      }
  
      onMounted(() => {
        searchUser(proxy.$route.query.keywords);
      });
  
      return {
        currentUserList,
      };
    },
  });
  </script>
  