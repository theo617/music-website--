<template>
    <div class="user-list">
      <el-table :data="userList" highlight-current-row>
        <el-table-column prop="avator" label="头像" width="100">
          <template #default="scope">
            <el-image :src="attachImageUrl(scope.row.avator)" style="width: 50px; height: 50px; border-radius: 50%;" @click.stop="goToUserHome(scope.row.id)" />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="sex" label="性别">
          <template #default="scope">
            <span>{{ scope.row.sex === 1 ? '男' : '女' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="introduction" label="个性签名">
        <template #default="scope">
          <span>{{ truncateIntroduction(scope.row.introduction) }}</span>
        </template>
      </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" @click="toggleFollow(scope.row)">
              <template v-if="isFollowing(scope.row.id)">
                <Check style="width:1em;height:1em; margin-right: 5px;fill:aliceblue" />
                已关注
              </template>
              <template v-else>
                <Plus style="width:1em;height:1em; margin-right: 5px;fill:aliceblue"/>
                关注
              </template>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  
  <script lang="ts">
import { defineComponent, ref, reactive, computed, onMounted } from "vue";
import { useStore } from "vuex";
  import { HttpManager } from "@/api";
  import { useRouter } from "vue-router";
  import { Plus, Check } from "@element-plus/icons-vue";
  
  export default defineComponent({
    components: {
    Plus,
    Check,
  },
    props: {
      userList: {
        type: Array,
        required: true,
      },
      
    },
    setup() {
      const router = useRouter();
      const store = useStore();
      const userId = computed(() => store.getters.userId);
      const followedUsers = ref([]);
      
      const goToUserHome = (userId) => {
        router.push({ path: `/user-home/${userId}` });
      };
      
      const fetchFollowedUsers = async () => {
      const result = await (HttpManager.myFollow(userId.value)) as ResponseBody;
      if (result.success) {
        followedUsers.value = result.data.map(user => user.followedId);
      }
    };


    const isFollowing = (followedId) => followedUsers.value.includes(followedId);

      const toggleFollow = async (user) => {
        fetchFollowedUsers();
      if (isFollowing(user.id)) {
        const result = await (HttpManager.deleteFollow(userId.value,user.id )) as ResponseBody;
        if (result.success) {
            fetchFollowedUsers();
        }
      } else {
        const result = await (HttpManager.addFollow({ followerId: userId.value, followedId: user.id })) as ResponseBody;
        if (result.success) {
            fetchFollowedUsers();
        }
      }
    };

    const truncateIntroduction = (introduction) => {
      if (!introduction) return "";
      return introduction.length > 10 ? introduction.slice(0, 10) + "..." : introduction;
    };
      function handleRowClick(row){
        //nothing
      }

      onMounted(() => {
      fetchFollowedUsers();
    });

      return {
        toggleFollow,
        attachImageUrl: HttpManager.attachImageUrl,
        goToUserHome,
        handleRowClick,
        isFollowing,
        truncateIntroduction,
      };
    },
  });
  </script>
  
  <style scoped>
  .user-list {
    width: 100%;
  }
  
  .user-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  
  .user-avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
  }
  
  .user-info {
    flex-grow: 1;
  }
  </style>
  