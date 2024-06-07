<template>
    <div class="personal-fans">
      <h2>我的粉丝列表</h2>
      <user-list :userList="fans"></user-list>
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
      const fans = ref([]);
  
      const fetchFans = async () => {
        const fansResult = (await HttpManager.followMe(userId.value)) as ResponseBody;
        if (fansResult.success) {
          const fanDetails = fansResult.data;
  
          const userPromises = fanDetails.map((fan) => HttpManager.getUserOfId(fan.followerId));
          const userResults = await Promise.all(userPromises) as ResponseBody[];
  
          fans.value = userResults.map(result => result.data[0]);
        }
      };
  
      onMounted(() => {
        fetchFans();
      });
  
      return {
        fans,
      };
    },
  });
  </script>
  
  <style scoped>
  .personal-fans {
    padding: 20px;
  }
  
  .personal-fans h2 {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
  }
  </style>
  