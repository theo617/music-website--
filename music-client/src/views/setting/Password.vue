<template>
  <div class="password-container">
    <h2 class="password-title">更改密码</h2>
    <el-form ref="passwordForm" label-width="100px" :model="form" :rules="rules" class="password-form">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input type="password" v-model="form.oldPassword" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="form.newPassword" />
      </el-form-item>
      <el-form-item label="密码确认" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" />
      </el-form-item>
      <el-form-item class="form-buttons">
        <el-button @click="clearData">重置</el-button>
        <el-button type="primary" @click="confirm">确认修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, computed, reactive } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { validatePassword } from "@/enums";

export default defineComponent({
  setup() {
    const store = useStore();
    const { proxy } = getCurrentInstance();
    const { goBack } = mixin();

    const form = reactive({
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    });
    const userId = computed(() => store.getters.userId);
    const userName = computed(() => store.getters.username);

    const validateCheck = (rule: any, value: any, callback: any) => {
      if (value === "") {
        callback(new Error("密码不能为空"));
      } else if (value !== form.newPassword) {
        callback(new Error("请输入正确密码"));
      } else {
        callback();
      }
    };
    const rules = reactive({
      oldPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
      newPassword: [{ validator: validatePassword, trigger: "blur", min: 3 }],
      confirmPassword: [{ validator: validateCheck, trigger: "blur", min: 3 }],
    });

    async function clearData() {
      form.oldPassword = "";
      form.newPassword = "";
      form.confirmPassword = "";
    }

    async function confirm() {
      let canRun = true;
      (proxy.$refs["passwordForm"] as any).validate((valid) => {
        if (!valid) return (canRun = false);
      });
      if (!canRun) return;


      const id = userId.value;
      const username = userName.value;
      const oldPassword = form.oldPassword;
      const password = form.newPassword;

      const result = (await HttpManager.updateUserPassword({id,username,oldPassword,password})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) goBack();
    }

    return {
      form,
      clearData,
      confirm,
      rules,
    };
  },
});
</script>

<style lang="scss" scoped>
.password-container {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: 50px auto;
}

.password-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.password-form {
  .el-form-item__label {
    font-weight: bold;
    color: #666;
  }

  .el-input, .el-date-picker, .el-select {
    border-radius: 5px;
  }

  .el-button {
    margin-right: 10px;
    border-radius: 5px;
  }

  .el-button--primary {
    background-color: #007BFF;
    border-color: #007BFF;
    &:hover {
      background-color: #0056b3;
      border-color: #0056b3;
    }
  }
}

.form-buttons {
  text-align: center;
}
</style>

