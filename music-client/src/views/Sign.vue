<template>
    <div class="body">
      <div class="login-register-container" :class="{ 'right-panel-active': isSignUpActive }">
        <!-- Sign Up Form -->
        <div class="container__form container--signup">
          <form @submit.prevent="handleSignUp" class="form">
            <h2 class="form__title">注册</h2>
            <div class="form-group">
              <input type="text" id="signUpUsername" v-model="signUpForm.username" class="input" placeholder="用户名" />
            </div>
            <div class="form-group">
              <input type="password" id="signUpPassword" v-model="signUpForm.password" class="input" placeholder="密码" />
            </div>
            <div class="form-group">
              <label>性别</label>
              <div>
                <label>
                  <input type="radio" v-model="signUpForm.sex" value="0" /> 女
                </label>
                <label>
                  <input type="radio" v-model="signUpForm.sex" value="1" /> 男
                </label>
                <label>
                  <input type="radio" v-model="signUpForm.sex" value="2" /> 保密
                </label>
              </div>
            </div>
            <div class="form-group">
              <label for="signUpBirth">生日</label>
              <input type="date" id="signUpBirth" v-model="signUpForm.birth" class="input" />
            </div>
            <button type="submit" class="btn">注册</button>
          </form>
        </div>
  
        <!-- Sign In Form -->
        <div class="container__form container--signin">
          <form @submit.prevent="handleSignIn" class="form">
            <h2 class="form__title">登录</h2>
            <div class="form-group">
              <input type="text" id="signInUsername" v-model="signInForm.username" class="input" placeholder="用户名" />
            </div>
            <div class="form-group">
              <input type="password" id="signInPassword" v-model="signInForm.password" class="input" placeholder="Password" />
            </div>
            <a href="#" class="link" @click.prevent="handleForgotPassword">忘记密码？</a>
            <button type="submit" class="btn">登录</button>
          </form>
        </div>
  
        <!-- Overlay -->
        <div class="container__overlay">
          <div class="overlay">
            <div class="overlay__panel overlay--left">
              <button class="btn" @click="toggleSignIn">切换登录</button>
            </div>
            <div class="overlay__panel overlay--right">
              <button class="btn" @click="toggleSignUp">切换注册</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, reactive, ref, getCurrentInstance } from 'vue';
  import { ElMessage } from 'element-plus';
  import { HttpManager } from "@/api";
  import { NavName, RouterName } from "@/enums";
  import mixin from "@/mixins/mixin";
  
  export default defineComponent({
    setup() {
      const isSignUpActive = ref(false);
      const { proxy } = getCurrentInstance();
      const { routerManager, changeIndex } = mixin();
  
      const signInForm = reactive({
        username: '',
        password: '',
      });
  
      const signUpForm = reactive({
      username: '',
      sex: '',
      birth: '',
      password: '',
      phoneNum: '',
      email: '',
      introduction: '',
      location: '',
    });
  
      async function handleSignIn() {
        try {
          const { username, password } = signInForm;
          const result = (await HttpManager.signIn({ username, password })) as ResponseBody;
          proxy.$message({
            message: result.message,
            type: result.type,
          });
  
          if (result.success) {
            proxy.$store.commit("setUserId", result.data[0].id);
            proxy.$store.commit("setUsername", result.data[0].username);
            proxy.$store.commit("setUserPic", result.data[0].avator);
            proxy.$store.commit("setToken", true);
            changeIndex(NavName.Home);
            routerManager(RouterName.Home, { path: RouterName.Home });
          }
        } catch (error) {
          console.error(error);
        }
      }
  
      async function handleSignUp() {
        try {
          const { username,password,sex,phoneNum,email,birth,introduction,location } = signUpForm;
          const result = (await HttpManager.SignUp({ username,password,sex,phoneNum,email,birth,introduction,location })) as ResponseBody;
          proxy.$message({
            message: result.message,
            type: result.type,
          });
  
          if (result.success) {
            toggleSignIn();
          }
        } catch (error) {
          console.error(error);
        }
      }
  
      function handleForgotPassword() {
        routerManager(RouterName.ForgotPassword, { path: RouterName.ForgotPassword });
      }
  
      function toggleSignUp() {
        isSignUpActive.value = true;
      }
  
      function toggleSignIn() {
        isSignUpActive.value = false;
      }
  
      return {
        isSignUpActive,
        signInForm,
        signUpForm,
        handleSignIn,
        handleSignUp,
        handleForgotPassword,
        toggleSignUp,
        toggleSignIn,
      };
    },
  });
  </script>
  
  <style scoped>
  .body {
    align-items: center;
    background-color: #7932c1;
    background-image: url('@/assets/images/background.jpg');
    background-attachment: fixed;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    display: grid;
    height: 100vh;
    place-items: center;
    margin: 0;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }
  
  .login-register-container {
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
    position: relative;
    overflow: hidden;
    width: 768px;
    max-width: 100%;
    height: 480px;
  }
  
  .container__form {
    height: 100%;
    position: absolute;
    top: 0;
    transition: all 0.6s ease-in-out;
  }
  
  .container--signin {
    left: 0;
    width: 50%;
    z-index: 2;
  }
  
  .container--signup {
    left: 0;
    opacity: 0;
    width: 50%;
    z-index: 1;
  }
  
  .right-panel-active .container--signin {
    transform: translateX(100%);
  }
  
  .right-panel-active .container--signup {
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
  }
  
  .container__overlay {
    height: 100%;
    left: 50%;
    overflow: hidden;
    position: absolute;
    top: 0;
    transition: transform 0.6s ease-in-out;
    width: 50%;
    z-index: 100;
  }
  
  .right-panel-active .container__overlay {
    transform: translateX(-100%);
  }
  
  .overlay {
    background-color: #504b4c;
    background-image: url('@/assets/images/background.jpg');
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
    color: #e3c6c6;
    position: relative;
    left: -100%;
    height: 100%;
    width: 200%;
    transform: translateX(0);
    transition: transform 0.6s ease-in-out;
  }
  
  .right-panel-active .overlay {
    transform: translateX(50%);
  }
  
  .overlay__panel {
    align-items: center;
    display: flex;
    flex-direction: column;
    height: 100%;
    justify-content: center;
    position: absolute;
    text-align: center;
    top: 0;
    transform: translateX(0);
    transition: transform 0.6s ease-in-out;
    width: 50%;
  }
  
  .overlay--left {
    transform: translateX(-20%);
  }
  
  .right-panel-active .overlay--left {
    transform: translateX(0);
  }
  
  .overlay--right {
    right: 0;
    transform: translateX(0);
  }
  
  .right-panel-active .overlay--right {
    transform: translateX(20%);
  }
  
  .btn {
    border-radius: 20px;
    border: 1px solid #0367a6;
    background-color: #0367a6;
    background-image: linear-gradient(90deg, #0367a6 0%, #008997 74%);
    color: #ffffff;
    cursor: pointer;
    font-size: 0.8rem;
    font-weight: bold;
    padding: 0.9rem 4rem;
    letter-spacing: 0.1rem;
    text-transform: uppercase;
    transition: transform 80ms ease-in;
  }
  
  .form > .btn {
    margin-top: 1.5rem;
  }
  
  .btn:active {
    transform: scale(0.95);
  }
  
  .btn:focus {
    outline: none;
  }
  
  .form {
    background-color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: 0 3rem;
    height: 100%;
    text-align: center;
  }
  
  .input {
    background-color: #fff;
    border: 1px solid #333;
    border-radius: 15px;
    padding: 0.9rem 0.9rem;
    margin: 0.7rem 0;
    width: 100%;
    color: #000;
  }
  
  .form__title {
    font-weight: bold;
    margin: 0;
    margin-bottom: 1.25rem;
  }
  
  .link {
    color: #333;
    font-size: 14px;
    margin: 15px 0;
    text-decoration: none;
  }
  
  .form-group {
    display: flex;
    align-items: center;
  }
  
  .form-group label {
    display: inline;
    font-size: small;
    margin-right: 15px;
  }
  </style>
  