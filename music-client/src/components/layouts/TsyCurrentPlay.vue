<template>
  <transition name="aside-fade">
    <div class="Tsy-current-play" v-if="showAside" ref="aside">
      <h2 class="title">当前播放列表</h2>
      <div class="control">共 {{ (currentPlayList && currentPlayList.length) || 0 }} 首</div>
      <ul class="menus">
        <li
          v-for="(item, index) in currentPlayList"
          :class="['menu-item', { 'is-play': songId === item.id }]"
          :key="index"
          @click="playMusic({
            id: item.id,
            url: item.url,
            pic: item.pic,
            index: index,
            name: item.name,
            lyric: item.lyric,
            currentSongList: currentPlayList,
          })"
          :style="{ backgroundColor: index % 2 === 0 ? '#f7f7f7' : '#ffffff' }"
        >
          {{ getSongTitle(item.name) }}
        </li>
      </ul>
      <!-- 添加虚线分割线 -->
      <hr class="divider" />
    </div>
  </transition>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";

export default defineComponent({
  setup() {
    const store = useStore();
    const { getSongTitle, playMusic } = mixin();

    const songId = computed(() => store.getters.songId); // 音乐 ID
    const currentPlayList = computed(() => store.getters.currentPlayList); // 当前播放
    const showAside = computed(() => store.getters.showAside); // 是否显示侧边栏
    const asideRef = ref<HTMLElement | null>(null);

    onMounted(() => {
      document.addEventListener('click', handleDocumentClick);
    });

    const handleDocumentClick = (e: MouseEvent) => {
      if (!asideRef.value || !showAside.value) return;
      if (!(e.target instanceof HTMLElement) || !asideRef.value.contains(e.target)) {
        store.commit('setShowAside', false);
      }
    };

    return {
      songId,
      currentPlayList,
      showAside,
      getSongTitle,
      playMusic,
      asideRef,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/Tsy-current-play.scss";

.Tsy-current-play {
  .divider {
    border: none;
    border-top: 1px dashed #ccc; /* 虚线分割线 */
    margin: 10px 0; /* 分割线的上下间距 */
  }

  .menus {
    padding: 0;
    list-style: none;

    .menu-item {
      padding: 10px;
      cursor: pointer;

      &.is-play {
        font-weight: bold; /* 当前播放的歌曲加粗 */
      }

      &:hover {
        background-color: #e0e0e0; /* 悬停时背景颜色 */
      }
    }
  }
}
</style>
