<template>
  <div class="setting-container">
    <el-tabs tab-position="left" class="setting-tabs">
      <el-tab-pane label="个人资料" class="content">
        <Personal-data></Personal-data>
      </el-tab-pane>
      <el-tab-pane label="更改密码" class="content">
        <Password></Password>
      </el-tab-pane>
      <el-tab-pane label="账号和安全" class="content1">
        <el-button type="danger" :icon="Delete" @click="cancelAccount">注销账号</el-button>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>


<script lang="ts">
import { defineComponent, getCurrentInstance, computed, reactive } from "vue";
import { Delete } from "@element-plus/icons-vue";
import PersonalData from "./PersonalData.vue";
import Password from "./Password.vue";
import { HttpManager } from "@/api";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { RouterName } from "@/enums";

export default defineComponent({
  components: {
    PersonalData,
    Password,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { routerManager } = mixin();

    const userId = computed(() => store.getters.userId);

    async function cancelAccount() {
      const result = (await HttpManager.deleteUser(userId.value)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      routerManager(RouterName.SignIn, { path: RouterName.SignIn });
      proxy.$store.commit("setToken", false);
    }

    return {
      Delete,
      cancelAccount,
    };
  },
});
</script>
<style lang="scss" scoped>
.setting-container {
  background-color: #fff;

  border-radius: 10px;

  max-width: 800px;
  margin: 50px auto;
}

.setting-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.content{
  margin-top:-60px;
}
.content1{
  margin-left:240px;
}
.setting-tabs {
  .el-tabs__header {
    background-color: #fff;
    border-radius: 10px 10px 0 0;
  }
  .el-tabs__item {
    border: none;
    font-size: 16px;
  }
  .el-tab-pane {
    padding: 20px;
  }
}

.el-button--danger {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
  &:hover {
    background-color: #d9363e;
    border-color: #d9363e;
  }
}
</style>
