import json
import polyline
import requests

departure = [19.8157059, 45.2571209]
destination = [19.804034, 45.2478236]

print(f'http://router.project-osrm.org/route/v1/car/{departure[0]},{departure[1]};{destination[0]},{destination[1]}?geometries=geojson&overview=full')
responseRoute = requests.get(f'http://router.project-osrm.org/route/v1/car/{departure[0]},{departure[1]};{destination[0]},{destination[1]}?geometries=geojson&overview=full')
routeGeoJSON = json.loads(responseRoute.text)
print(routeGeoJSON)
coordinates = [*routeGeoJSON['routes'][0]['geometry']['coordinates']]
print()
coordinates = [[lat,lon] for lon, lat in coordinates]
print(polyline.encode(coordinates))