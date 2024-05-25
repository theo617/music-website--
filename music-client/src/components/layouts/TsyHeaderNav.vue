<template>
  <nav class="header-nav">
    <ul>
      <li v-for="(item, index) in styleList" :key="index" @click="handleClick(item)">
        <span :class="{ active: item.name === activeName }">{{ item.name }}</span>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'TsyHeaderNav',
  props: {
    styleList: {
      type: Array,
      required: true,
    },
    activeName: {
      type: String,
      required: true,
    },
  },
  methods: {
    handleClick(item) {
      this.$emit('click', item.path, item.name);
    },
  },
};
</script>

<style scoped>
.header-nav {
  display: flex;
  justify-content: center;
  align-items: center;

  ul {
    list-style: none;
    display: flex;
    margin: 0;
    padding: 0;

    li {
      margin: 0 20px;
      cursor: pointer;

      span {
        color: #ccc;
        font-size: 18px;
        transition: transform 0.2s, box-shadow 0.2s;
        display: inline-block;
        transform-origin: center; /* 确保放缩效果只影响自身 */
      }

      span.active,
      span:hover {
        color: #fff;
        background-color: #333;
        border-radius: 3px;
        padding: 5px 10px;
        
      }

      span:hover {
        transform: scale(1.1); /* 放缩效果 */
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.2); /* 阴影效果 */
      }
    }
  }
}
</style>
