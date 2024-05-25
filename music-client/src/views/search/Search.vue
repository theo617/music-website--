<template>
  <div class="search">
    <Tsy-nav :styleList="searchNavList" :activeName="activeName" @click="handleChangeView"></Tsy-nav>
    <component class="search-list" :is="currentView"></component>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import TsyNav from "@/components/layouts/TsyNav.vue";
import SearchSong from "./SearchSong.vue";
import SearchSongList from "./SearchSongList.vue";

export default defineComponent({
  components: {
    TsyNav,
    SearchSong,
    SearchSongList,
  },
  data() {
    return {
      searchNavList: [
        {
          name: "歌曲",
          value: "SearchSong",
        },
        {
          name: "歌单",
          value: "SearchSongList",
        },
      ],
      activeName: "歌曲",
      currentView: "SearchSong",
    };
  },
  methods: {
    handleChangeView(item) {
      this.activeName = item.name;
      this.currentView = item.value;
    },
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.search {
  margin: auto;
  width: 900px;
  
  .search-list {
    min-height: 480px;
  }
}
</style>
