-- =============================================================================
-- Habit_Up 2.0.0 — Database Initialization Script
-- =============================================================================
-- Usage:
--   mysql -u <user> -p <database> < database/init.sql
-- Or paste directly into MySQL Workbench / Railway query console.
-- =============================================================================

SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

-- -----------------------------------------------------------------------------
-- Database
-- -----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `habit_up_2`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `habit_up_2`;

-- =============================================================================
-- TABLE: users
-- =============================================================================
CREATE TABLE IF NOT EXISTS `users` (
    `id`                     BIGINT          NOT NULL AUTO_INCREMENT,
    `full_name`              VARCHAR(255)    NOT NULL,
    `email`                  VARCHAR(255)    NOT NULL UNIQUE,
    `password`               VARCHAR(255)    NOT NULL,
    `bio`                    VARCHAR(500)    DEFAULT NULL,
    `avatar_url`             VARCHAR(500)    DEFAULT NULL,
    `location`               VARCHAR(255)    DEFAULT NULL,
    `phone_number`           VARCHAR(50)     DEFAULT NULL,
    `is_private`             TINYINT(1)      NOT NULL DEFAULT 0,
    `notifications_enabled`  TINYINT(1)      NOT NULL DEFAULT 1,
    `created_at`             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: achievements
-- =============================================================================
CREATE TABLE IF NOT EXISTS `achievements` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT,
    `title`         VARCHAR(255)    NOT NULL,
    `description`   VARCHAR(500)    NOT NULL,
    `icon`          VARCHAR(255)    NOT NULL,
    `category`      VARCHAR(100)    NOT NULL,
    `required_days` INT             NOT NULL,
    `unlocked_at`   DATETIME        DEFAULT NULL,
    `user_id`       BIGINT          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_achievements_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: posts
-- =============================================================================
CREATE TABLE IF NOT EXISTS `posts` (
    `id`         BIGINT          NOT NULL AUTO_INCREMENT,
    `content`    VARCHAR(500)    NOT NULL,
    `likes`      INT             NOT NULL DEFAULT 0,
    `created_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`    BIGINT          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_posts_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: comments
-- =============================================================================
CREATE TABLE IF NOT EXISTS `comments` (
    `id`         BIGINT          NOT NULL AUTO_INCREMENT,
    `content`    VARCHAR(500)    NOT NULL,
    `created_at` DATETIME        DEFAULT NULL,
    `user_id`    BIGINT          DEFAULT NULL,
    `post_id`    BIGINT          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_comments_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_post`
        FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: habits
-- =============================================================================
CREATE TABLE IF NOT EXISTS `habits` (
    `id`             BIGINT          NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(255)    NOT NULL,
    `description`    VARCHAR(500)    DEFAULT NULL,
    `category`       VARCHAR(100)    NOT NULL,
    `start_date`     DATETIME        NOT NULL,
    `daily_expense`  DOUBLE          NOT NULL DEFAULT 0,
    `total_saved`    DOUBLE          DEFAULT NULL,
    `currency`       VARCHAR(10)     NOT NULL DEFAULT 'MXN',
    `is_active`      TINYINT(1)      NOT NULL DEFAULT 1,
    `created_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `current_streak` INT             NOT NULL DEFAULT 0,
    `user_id`        BIGINT          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_habits_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: relapses
-- =============================================================================
CREATE TABLE IF NOT EXISTS `relapses` (
    `id`          BIGINT          NOT NULL AUTO_INCREMENT,
    `reason`      VARCHAR(255)    NOT NULL,
    `comment`     VARCHAR(1000)   DEFAULT NULL,
    `emotion`     VARCHAR(255)    NOT NULL,
    `relapsed_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`     BIGINT          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_relapses_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- TABLE: users_achievements  (legacy / reporting join table)
-- =============================================================================
CREATE TABLE IF NOT EXISTS `users_achievements` (
    `user_id`        BIGINT   NOT NULL,
    `achievement_id` BIGINT   NOT NULL,
    PRIMARY KEY (`user_id`, `achievement_id`),
    CONSTRAINT `fk_ua_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_ua_achievement`
        FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- SAMPLE DATA
-- =============================================================================

-- -----------------------------------------------------------------------------
-- users  (passwords are BCrypt hashes of "password123")
-- -----------------------------------------------------------------------------
INSERT INTO `users`
    (`id`, `full_name`, `email`, `password`, `bio`, `avatar_url`, `location`, `phone_number`, `is_private`, `notifications_enabled`, `created_at`)
VALUES
    (1, 'Alex García',    'alex@habitup.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc5AlL2i', 'Dejando el cigarro un día a la vez 🚭', NULL, 'Ciudad de México', '5551234567', 0, 1, '2024-01-15 10:00:00'),
    (2, 'María López',   'maria@habitup.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc5AlL2i', 'Corriendo hacia una vida más sana 🏃‍♀️', NULL, 'Guadalajara',       '5559876543', 0, 1, '2024-01-20 09:30:00'),
    (3, 'Carlos Ruiz',   'carlos@habitup.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc5AlL2i', 'Sin alcohol desde enero 💪',            NULL, 'Monterrey',         '5554567890', 0, 1, '2024-02-01 08:00:00'),
    (4, 'Laura Martínez','laura@habitup.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc5AlL2i', 'Meditando cada mañana 🧘‍♀️',            NULL, 'Puebla',            '5552345678', 1, 1, '2024-02-10 11:00:00'),
    (5, 'Diego Torres',  'diego@habitup.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVyc5AlL2i', 'Construyendo mejores hábitos 🌱',       NULL, 'Tijuana',           '5556789012', 0, 0, '2024-02-15 14:00:00');

-- -----------------------------------------------------------------------------
-- habits
-- -----------------------------------------------------------------------------
INSERT INTO `habits`
    (`id`, `name`, `description`, `category`, `start_date`, `daily_expense`, `total_saved`, `currency`, `is_active`, `created_at`, `current_streak`, `user_id`)
VALUES
    (1,  'Dejar de fumar',          'Adiós al cigarro para siempre',              'TABACO',    '2024-01-15 08:00:00', 80.00,  NULL,    'MXN', 1, '2024-01-15 10:00:00', 45, 1),
    (2,  'Sin alcohol',             'Vida más clara sin alcohol',                 'ALCOHOL',   '2024-02-01 00:00:00', 150.00, NULL,    'MXN', 1, '2024-02-01 08:00:00', 28, 3),
    (3,  'Ejercicio diario',        'Correr 5 km cada mañana',                    'EJERCICIO', '2024-01-20 06:00:00', 0.00,   NULL,    'MXN', 1, '2024-01-20 09:30:00', 38, 2),
    (4,  'Meditación matutina',     'Meditar 20 minutos al despertar',            'BIENESTAR', '2024-02-10 07:00:00', 0.00,   NULL,    'MXN', 1, '2024-02-10 11:00:00', 18, 4),
    (5,  'Sin redes sociales',      'Reducir el tiempo en redes a 30 min/día',   'DIGITAL',   '2024-02-15 00:00:00', 0.00,   NULL,    'MXN', 1, '2024-02-15 14:00:00', 13, 5),
    (6,  'Lectura diaria',          'Leer al menos 30 páginas cada día',          'EDUCACION', '2024-01-15 21:00:00', 0.00,   NULL,    'MXN', 1, '2024-01-15 10:00:00', 45, 1),
    (7,  'Sin comida chatarra',     'Eliminar frituras y refrescos',              'NUTRICION', '2024-02-01 00:00:00', 60.00,  NULL,    'MXN', 0, '2024-02-01 08:00:00', 10, 3);

-- -----------------------------------------------------------------------------
-- achievements
-- -----------------------------------------------------------------------------
INSERT INTO `achievements`
    (`id`, `title`, `description`, `icon`, `category`, `required_days`, `unlocked_at`, `user_id`)
VALUES
    (1,  'Primer Día',          'Completaste tu primer día sin recaídas',       '🌱', 'INICIO',    1,   '2024-01-16 00:00:00', 1),
    (2,  'Una Semana',          '7 días consecutivos de progreso',              '⭐', 'PROGRESO',  7,   '2024-01-22 00:00:00', 1),
    (3,  'Dos Semanas',         '14 días de constancia',                        '🔥', 'PROGRESO',  14,  '2024-01-29 00:00:00', 1),
    (4,  'Un Mes',              '30 días de transformación',                    '🏆', 'LOGRO',     30,  '2024-02-14 00:00:00', 1),
    (5,  'Primer Día',          'Completaste tu primer día sin recaídas',       '🌱', 'INICIO',    1,   '2024-01-21 00:00:00', 2),
    (6,  'Una Semana',          '7 días consecutivos de progreso',              '⭐', 'PROGRESO',  7,   '2024-01-27 00:00:00', 2),
    (7,  'Dos Semanas',         '14 días de constancia',                        '🔥', 'PROGRESO',  14,  '2024-02-03 00:00:00', 2),
    (8,  'Primer Día',          'Completaste tu primer día sin recaídas',       '🌱', 'INICIO',    1,   '2024-02-02 00:00:00', 3),
    (9,  'Una Semana',          '7 días consecutivos de progreso',              '⭐', 'PROGRESO',  7,   '2024-02-08 00:00:00', 3),
    (10, 'Primer Día',          'Completaste tu primer día sin recaídas',       '🌱', 'INICIO',    1,   '2024-02-11 00:00:00', 4),
    (11, 'Primer Día',          'Completaste tu primer día sin recaídas',       '🌱', 'INICIO',    1,   '2024-02-16 00:00:00', 5),
    (12, 'Maestro del Hábito',  '100 días de dedicación absoluta',              '👑', 'ELITE',     100, NULL,                  NULL);

-- -----------------------------------------------------------------------------
-- posts
-- -----------------------------------------------------------------------------
INSERT INTO `posts`
    (`id`, `content`, `likes`, `created_at`, `user_id`)
VALUES
    (1, '¡Día 45 sin fumar! Nunca pensé llegar hasta aquí. El apoyo de esta comunidad lo es todo 🙌', 12, '2024-03-01 10:30:00', 1),
    (2, 'Consejo del día: cuando sientas el antojo, sal a caminar 10 minutos. Funciona de verdad 🚶‍♂️', 8,  '2024-03-02 09:00:00', 1),
    (3, 'Completé mi semana 5 de running. 5 km cada mañana. ¡El cuerpo ya lo pide solo! 🏃‍♀️💨',       15, '2024-03-01 07:45:00', 2),
    (4, 'Un mes sin alcohol. Me siento más claro, más presente. Vale totalmente la pena 💪',           10, '2024-03-03 20:00:00', 3),
    (5, 'La meditación matutina cambió mi forma de enfrentar el día. 18 días seguidos 🧘‍♀️✨',          7,  '2024-02-28 08:00:00', 4),
    (6, 'Reducir redes sociales fue más difícil de lo que creía, pero ya noto la diferencia 📵',       5,  '2024-02-28 19:00:00', 5);

-- -----------------------------------------------------------------------------
-- comments
-- -----------------------------------------------------------------------------
INSERT INTO `comments`
    (`id`, `content`, `created_at`, `user_id`, `post_id`)
VALUES
    (1,  '¡Increíble Alex! Tú puedes 💪',                                    '2024-03-01 11:00:00', 2, 1),
    (2,  'Ese consejo me salvó ayer, gracias 🙏',                            '2024-03-02 10:00:00', 3, 2),
    (3,  '¡Qué inspiración! Yo voy en la semana 3 🏃‍♂️',                     '2024-03-01 08:30:00', 1, 3),
    (4,  'Felicidades Carlos, un mes es un logro enorme 🎉',                 '2024-03-03 21:00:00', 1, 4),
    (5,  'La meditación también me cambió la vida, sigue así Laura ✨',      '2024-02-28 09:00:00', 2, 5),
    (6,  'Yo también lo intenté y es durísimo. ¡Ánimo Diego! 💙',           '2024-02-28 20:00:00', 3, 6),
    (7,  '45 días es una locura, eres un ejemplo para todos aquí 🌟',       '2024-03-01 12:00:00', 4, 1),
    (8,  'Comparto ese consejo con mi grupo, gracias Alex',                  '2024-03-02 11:30:00', 5, 2);

-- -----------------------------------------------------------------------------
-- relapses
-- -----------------------------------------------------------------------------
INSERT INTO `relapses`
    (`id`, `reason`, `comment`, `emotion`, `relapsed_at`, `user_id`)
VALUES
    (1, 'Estrés laboral',    'Tuve una semana muy difícil en el trabajo y no pude resistir el antojo del cigarro.',  'Decepcionado', '2024-01-25 22:00:00', 1),
    (2, 'Entorno social',    'Salí con amigos que fuman y no pude decir que no en ese momento.',                     'Avergonzado',  '2024-02-05 23:30:00', 1),
    (3, 'Ansiedad',          'La ansiedad me ganó. Mañana vuelvo a empezar con más fuerza.',                         'Triste',       '2024-02-10 21:00:00', 3),
    (4, 'Aburrimiento',      'No tenía nada que hacer y caí en el vicio sin darme cuenta.',                          'Frustrado',    '2024-02-20 16:00:00', 5);

-- -----------------------------------------------------------------------------
-- users_achievements  (mirrors the achievements already assigned above)
-- -----------------------------------------------------------------------------
INSERT INTO `users_achievements` (`user_id`, `achievement_id`)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (2, 5),
    (2, 6),
    (2, 7),
    (3, 8),
    (3, 9),
    (4, 10),
    (5, 11);

-- =============================================================================
-- Re-enable foreign key checks
-- =============================================================================
SET foreign_key_checks = 1;
