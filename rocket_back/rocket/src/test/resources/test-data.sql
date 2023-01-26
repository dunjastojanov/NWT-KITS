INSERT INTO public.role(id, role) VALUES (1, 'CLIENT');
INSERT INTO public.role(id, role) VALUES (2, 'DRIVER');
INSERT INTO public.role(id, role) VALUES (3, 'ADMINISTRATOR');

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('1','false', 'Novi Sad','biljana@gmail.com', 'Biljana', 'Radulov', '$2a$12$6wFZb50rAgDaEO3g6sP2eeIlhd/LceYci9zvYHGspI/UqUsPdkS3m', '0611111111', '', '2'); --Biljana123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('2', 'false', 'Novi Sad', 'dragan@gmail.com', 'Dragan', 'Dragic', '$2a$12$2fG2KR6rmfyhB294SF83Qe08mbbcKLmNTdB/EEvy425GvVyZci..u', '0622222222', '', '4'); --Dragan123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('3', 'false', 'Novi Sad', 'ivana@gmail.com', 'Ivana', 'Berkovic', '$2a$12$8U6A0r2iRNZAXIBA7KtPae3q10SqiklbpRVzH48Yh3P0OHM2dh.L.', '0633333333', '', '6'); --Ivana123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('4', 'false', 'Novi Sad', 'milan@gmail.com', 'Milan', 'Nikolic', '$2a$12$yzZ2FnMjpYxRPMIrsJbYPupcTLBS.9lgFYSbJnufDHYbrSElsRaaK', '0644444444', '', '8'); --Milan123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('5', 'false', 'Novi Sad', 'slavica@gmail.com', 'Slavica', 'Slavic', '$2a$12$yBPgSE8Dy1e67uFB1Dm3buIbunwRVFmnQvmLrVOKje.1OlD1hmofK', '0615555555', '', '10'); --Slavica123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('6', 'false', 'Novi Sad', 'bogdana@gmail.com', 'Bogdana', 'Vujic', '$2a$12$S/MU.xyjhDYbTaWxwOdwFuzrIE8smLunaHp8dQwc3qac9Ts.Fj/he', '0626666666', '', '1'); --Bogana123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('7', 'false', 'Novi Sad', 'vladimir@gmail.com', 'Vladimir', 'Sinik', '$2a$12$81NhSxwFsmae4LCCLRx9n.yVXQevzNJpYuP2YZqInZbg0d34D7wQa', '0637777777', '', '3'); --Vladimir123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('8', 'false', 'Novi Sad', 'dalibor@gmail.com', 'Dalibor', 'Dobrilovic', '$2a$12$GFKQyIW0Xj.nA.tc9wm9KOtUbxsRj4hefs6mBVin/5RE1UePrZItW', '0648888888', '', '5'); --Dalibor123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('9', 'false', 'Novi Sad', 'eleonora@gmail.com', 'Eleonora', 'Desnica', '$2a$12$7boxBTZJ46nKO0OHm4jpe.63yXNqLnuHqCt5cDsRixFwAySxh2naa', '0619999999', '', '7'); --Eleonora123

INSERT INTO public.app_user(
    id, blocked, city, email, first_name, last_name, password, phone_number, profile_picture, tokens)
VALUES ('10', 'false', 'Novi Sad', 'jelena@gmail.com', 'Jelena', 'Stojanov', '$2a$12$GgX0q3pjQILMMunVnwtGXupu8l6s9iBgbGkTgfibs1EhDrId0v8Cy', '0620000000', '', '9'); --Jelena123

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('1', '3');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('2', '2');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('3', '2');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('4', '2');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('5', '1');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('6', '1');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('7', '1');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('8', '1');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('9', '1');

INSERT INTO public.app_user_roles(
    user_id, roles_id)
VALUES ('10', '1');


INSERT INTO public.vehicle(
    id, latitude, longitude, kid_friendly, pet_friendly, status, vehicle_type, driver_id)
VALUES ('1','45.248130' , '19.849070', 'true', 'true', 'INACTIVE', 'CARAVAN', '2');

INSERT INTO public.vehicle(
    id, latitude, longitude, kid_friendly, pet_friendly, status, vehicle_type, driver_id)
VALUES ('2', '45.248130', '19.849070', 'false', 'false', 'INACTIVE', 'CONVERTIBLE', '3');

INSERT INTO public.vehicle(
    id, latitude, longitude, kid_friendly, pet_friendly, status, vehicle_type, driver_id)
VALUES ('3', '45.248130', '19.849070',  'true', 'false', 'INACTIVE', 'LIMOUSINE', '4');

INSERT INTO public.ride(
    id, duration, end_time, kid_friendly, length, now, pet_friendly, price, route_location, split_fare, start_time, status, vehicle_type_requested, vehicle_id)
VALUES ('13', '180', null, 'true', '1538.2', 'true', 'true', '520', 'smdsGwz~wBxAEA{EoFHkAf@_Al@`@gO@iES}NYyCgA_F`BeAz@_AdB{Dj@eBxGjGfBgF', 'false', '2023-01-24 21:20:44.000000', 'REQUESTED', 'CARAVAN','true');

INSERT INTO public.passenger(id, user_riding_status, user_id, ride_id)
VALUES ('27', 'ACCEPTED', '7', '13' );

INSERT INTO public.passenger(id, user_riding_status, user_id, ride_id)
VALUES ('28', 'ACCEPTED', '8', '13' );

INSERT INTO public.passenger(id, user_riding_status, user_id, ride_id)
VALUES ('29', 'ACCEPTED', '10', '13' );

INSERT INTO public.destination(id, address, latitude, longitude, ride_id)
VALUES ('25', 'Brace Krkljus 7 Novi Sad', '45.2477844', '19.8241151', '13');

INSERT INTO public.destination(id, address, latitude, longitude, ride_id)
VALUES ('26', 'Gogoljeva 18 Novi Sad', '45.2462784', '19.8346671', '13');
