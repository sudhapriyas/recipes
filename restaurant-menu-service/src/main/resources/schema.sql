CREATE TABLE RECIPES(
ID NUMBER PRIMARY KEY,
NAME VARCHAR2(30) NOT NULL,
FOOD_TYPE VARCHAR2(10) NOT NULL,
SERVING_CAPACITY NUMBER,
INSTRUCTIONS VARCHAR2(50)
);
CREATE TABLE RECIPE_ENTITY_INGREDIENTS (
RECIPE_ENTITY_ID NUMBER NOT NULL,
NAME VARCHAR2(15)
);
