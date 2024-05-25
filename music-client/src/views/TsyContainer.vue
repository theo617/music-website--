<template>
  <el-container>
    <el-header>
      <Tsy-header></Tsy-header>
    </el-header>
    <el-main>
      <router-view />
      <Tsy-current-play></Tsy-current-play>
      <Tsy-play-bar></Tsy-play-bar>
      <Tsy-scroll-top></Tsy-scroll-top>
      <Tsy-audio></Tsy-audio>
    </el-main>
    <el-footer>
      <Tsy-footer></Tsy-footer>
    </el-footer>
  </el-container>
</template>

<script lang="ts" setup>
import { getCurrentInstance } from "vue";
import TsyHeader from "@/components/layouts/TsyHeader.vue";
import TsyCurrentPlay from "@/components/layouts/TsyCurrentPlay.vue";
import TsyPlayBar from "@/components/layouts/TsyPlayBar.vue";
import TsyScrollTop from "@/components/layouts/TsyScrollTop.vue";
import TsyFooter from "@/components/layouts/TsyFooter.vue";
import TsyAudio from "@/components/layouts/TsyAudio.vue";

const { proxy } = getCurrentInstance();

if (sessionStorage.getItem("dataStore")) {
  proxy.$store.replaceState(Object.assign({}, proxy.$store.state, JSON.parse(sessionStorage.getItem("dataStore"))));
}

window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("dataStore", JSON.stringify(proxy.$store.state));
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.el-container {
  min-height: calc(100% - 60px);
}
.el-header {
  padding: 0;
}
.el-main {
  padding-left: 0;
  padding-right: 0;
}
</style>
