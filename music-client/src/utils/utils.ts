import * as Rgbaster from 'rgbaster';

export const extractColors = async (imageUrl) => {
  const result = await Rgbaster.colors(imageUrl, { ignore: ['rgb(255,255,255)', 'rgb(0,0,0)'] });
  return result.map(color => color.color);
};

// 解析歌词
export function parseLyric(text) {
  const lines = text.split("\n");
  const pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g;
  const result = [];

  // 对于歌词格式不对的特殊处理
  if (!/\[.+\]/.test(text)) {
    return [text];
  }
  for (const item of lines) {
    if (pattern.test(item)) {
      const value = item.replace(pattern, ""); // 存歌词
      result.push(value);
    }
  }
  return result;
}
