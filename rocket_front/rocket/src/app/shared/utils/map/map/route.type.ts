export type Route = {
  distance: number;
  duration: number;
  geometry: { coordinates: [number, number][] };
  selected: boolean;
};
