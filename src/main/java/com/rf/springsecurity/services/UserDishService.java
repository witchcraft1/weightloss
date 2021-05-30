package com.rf.springsecurity.services;

import com.rf.springsecurity.entity.dish.Dish;
import com.rf.springsecurity.entity.MyUser;
import com.rf.springsecurity.entity.dish.Mealtime;
import com.rf.springsecurity.entity.exercise.Exercise;
import com.rf.springsecurity.entity.user_dish.UserDish;
import com.rf.springsecurity.repository.UserDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserDishService {
    private final UserDishRepository userDishRepository;

    @Autowired
    public UserDishService(UserDishRepository userDishRepository){
        this.userDishRepository = userDishRepository;
    }
    public void save(UserDish userDish){
        userDishRepository.save(userDish);
    }
    public List<UserDish> findAllByUser(MyUser user){
        return userDishRepository.findAllByUser(user);
    }

    public List<UserDish> findAllByUserAndDate(MyUser user, LocalDate localDate){
        return userDishRepository.findAllByUserAndDate(user, localDate);
    }

    public List<UserDish> findAllByDish(Dish dish){
        return userDishRepository.findAllByDish(dish);
    }
    public UserDish findByDishAndUser(Dish dish, MyUser user){
        return userDishRepository.findByDishAndUser(dish,user);
    }

    @Transactional
    public void deleteByDishAndUser(Dish dish, MyUser user){
        userDishRepository.deleteUserDishByDishAndUser(dish,user);
    }

    @Transactional
    public void deleteById(Long id, MyUser user, Dish dish){
        userDishRepository.deleteByIdAndUserAndDish(id,user,dish);
    }

    @Transactional
    public void deleteAllByDish(Dish dish){
        userDishRepository.deleteAllByDish(dish);
    }

    public List<UserDish> fitUserDishesToPortions(List<UserDish> userDishes){
        userDishes.forEach(userDish -> {
            Dish userDish_ = userDish.getDish();

            double times_bigger = userDish.getValue() * 1. * userDish.getPortion().getGramsInPortion() / userDish_.getPortion().getGramsInPortion();

            userDish.setDish(
                    Dish.builder()
                            .id(userDish_.getId())
                            .name(userDish_.getName())
                            .calories((int) (userDish_.getCalories() * times_bigger))
                            .carbs((int)(userDish_.getCarbs() * times_bigger))
                            .fat((int)(userDish_.getFat() * times_bigger))
                            .protein((int)(userDish_.getProtein()*times_bigger))
                            .build()
            );
        });
        return userDishes;
    }

    public Dish getSummary(List<UserDish> userDishes){
        int calories = 0;
        int carbs = 0;
        int fats = 0;
        int protein = 0;
        for (UserDish userDish : userDishes) {
            calories+=userDish.getDish().getCalories();
            carbs+=userDish.getDish().getCarbs();
            fats+=userDish.getDish().getFat();
            protein+=userDish.getDish().getProtein();
        }

        return Dish.builder()
                    .calories(calories)
                    .carbs(carbs)
                    .fat(fats)
                    .protein(protein)
                .build();
    };

    public Map<String, Integer> calcCaloriesForEachMealtime(List<UserDish> userDishes) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Mealtime mealtime : Mealtime.values()) {
            map.put(mealtime.name(),0);
        }

        userDishes.forEach(userDish -> {
            map.computeIfPresent(userDish.getMealtime().name(),
                    (k,v)->v+userDish.getDish().getCalories());
        });

        return map;
    }

    public Map<Mealtime, List<String>> getDishesByDay(int day) {
        switch (day){
            case 1: return get1DayDishes();
            case 2: return get2DayDishes();
            case 3: return get3DayDishes();
            case 4: return get4DayDishes();
            case 5: return get5DayDishes();
            case 6: return get6DayDishes();
            case 7: return get7DayDishes();
            case 8: return get8DayDishes();
            case 9: return get9DayDishes();
            case 10: return get10DayDishes();
            case 11: return get11DayDishes();
            case 12: return get12DayDishes();
            case 13: return get13DayDishes();
            case 14: return get14DayDishes();
            case 15: return get15DayDishes();
            case 16: return get16DayDishes();
            case 17: return get17DayDishes();
            case 18: return get18DayDishes();
            case 19: return get19DayDishes();
            case 20: return get20DayDishes();
            case 21: return get21DayDishes();
            case 22: return get22DayDishes();
            case 23: return get23DayDishes();
            case 24: return get24DayDishes();
            case 25: return get25DayDishes();
            case 26: return get26DayDishes();
            case 27: return get27DayDishes();
            case 28: return get28DayDishes();
            case 29: return get29DayDishes();
            case 30: return get30DayDishes();
            default: return null;
        }
    }

    private Map<Mealtime, List<String>> get30DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get29DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get28DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get27DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get26DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get25DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get24DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get23DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get22DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get21DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get20DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get19DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get18DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get17DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get16DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get15DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get14DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get13DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get12DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get11DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get10DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get9DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get8DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get7DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get6DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get5DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get4DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get3DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>> get2DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList(""));
        map.put(Mealtime.SUPPER, Arrays.asList("f"));
        map.put(Mealtime.SNACKS, Arrays.asList(""));

        return map;
    }

    private Map<Mealtime, List<String>>  get1DayDishes() {
        Map<Mealtime, List<String>> map = new LinkedHashMap<>();

        map.put(Mealtime.BREAKFAST, Arrays.asList("1 cup of tea or black coffee without sugar and milk", "Oatmeal porridge"));
        map.put(Mealtime.DINNER, Arrays.asList("2 pieces of whole-grain toast with vegetables(onions, tomato, lettuce)", "Any grilled or boiled lean meats"));
        map.put(Mealtime.SUPPER, Collections.singletonList("Chicken Salad"));
        map.put(Mealtime.SNACKS, Arrays.asList("Any no-salted nuts", "1 piece of cheese"));

        return map;
    }
}
