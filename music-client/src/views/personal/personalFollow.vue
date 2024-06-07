<template>
    <div class="personal-follow">
      <h2>我的关注列表</h2>
      <user-list :userList="followedUsers"></user-list>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, computed, onMounted } from "vue";
  import { useStore } from "vuex";
  import UserList from "@/components/UserList.vue";
  import { HttpManager } from "@/api";
  
  export default defineComponent({
    components: {
      UserList,
    },
    setup() {
      const store = useStore();
      const userId = computed(() => store.getters.userId);
      const followedUsers = ref([]);
  
      const fetchFollowedUsers = async () => {
        const followResult = (await HttpManager.myFollow(userId.value)) as ResponseBody;
        if (followResult.success) {
          const followDetails = followResult.data;
  
          const userPromises = followDetails.map((follow) => HttpManager.getUserOfId(follow.followedId));
          const userResults = await Promise.all(userPromises) as ResponseBody[];
  
          followedUsers.value = userResults.map(result => result.data[0]);
        }
      };
  
      onMounted(() => {
        fetchFollowedUsers();
      });
  
      return {
        followedUsers,
      };
    },
  });
  </script>
  
  <style scoped>
  .personal-follow {
    padding: 20px;
  }
  
  .personal-follow h2 {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
  }
  </style>
  