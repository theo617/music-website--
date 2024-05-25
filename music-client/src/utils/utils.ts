import * as Rgbaster from 'rgbaster';

export const extractColors = async (imageUrl) => {
  const result = await Rgbaster.colors(imageUrl, { ignore: ['rgb(255,255,255)', 'rgb(0,0,0)'] });
  return result.map(color => color.color);
};
