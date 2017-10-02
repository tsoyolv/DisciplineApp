INSERT INTO `disciplinedb`.`user` (`id`, `username`, `password`, `first_name`, `second_name`, `last_name`, `email`) VALUES ('1', 'olts', '1', 'Олег', 'Цой', 'Вячеславович', 'tsoyolv');

INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('1', 'Monday');
INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('2', 'Tuesday');
INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('3', 'Wednesday');
INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('4', 'Thursday');
INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('5', 'Friday');
INSERT INTO `disciplinedb`.`period` (`id`, `day`) VALUES ('6', 'Saturday');

INSERT INTO `disciplinedb`.`habit` (`id`, `name`, `difficulty`, `user_id`) VALUES ('1', 'Вставать в 7 утра', '6', '1');

INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('1', '1', '1');
INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('2', '1', '2');
INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('3', '1', '3');
INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('4', '1', '4');
INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('5', '1', '5');
INSERT INTO `disciplinedb`.`habit_to_period` (`id`, `habit_id`, `period_id`) VALUES ('6', '1', '6');

