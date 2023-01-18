import { Destination } from 'src/app/interfaces/Destination';

export type Route = {
  distance: number;
  duration: number;
  geometry: { coordinates: [number, number][] };
  selected: boolean;
};

export const baseUrl = 'http://router.project-osrm.org/route/v1/car/';
export const options = {
  alternatives: 'true',
  geometries: 'geojson',
};
export const url = (start: Destination, end: Destination) => {
  return `${baseUrl}${start.longitude},${start.latitude};${end.longitude},${
    end.latitude
  }?${Object.keys(options)
    .map((key) => `${key}=${options[key as keyof typeof options]}`)
    .join('&')}`;
};
