CREATE schema if not exists disciplinedb;

DROP TABLE `disciplinedb`.`user`;
DROP TABLE `disciplinedb`.`habit`;

CREATE TABLE IF NOT EXISTS `disciplinedb`.`user` (
  `id` BIGINT NOT NULL,
  `create_when` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `login` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `first_name` VARCHAR(45) NULL,
  `second_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `task_score` INT NULL,
  `habit_score` INT NULL,
  `score` INT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `disciplinedb`.`habit` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(45) NULL,
  `difficulty` INT NULL,
  `user_id` BIGINT NOT NULL,
  `create_when` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` LONGTEXT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_habit_user`
  FOREIGN KEY (`user_id`)
  REFERENCES `disciplinedb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);