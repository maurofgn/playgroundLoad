-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema booking
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema booking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `booking` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `booking` ;

-- -----------------------------------------------------
-- Table `booking`.`Persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`Persona` (
  `idPersona` INT NOT NULL COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  `cognome` VARCHAR(45) NULL COMMENT '',
  `citta` VARCHAR(45) NULL COMMENT '',
  `prov` VARCHAR(45) NULL COMMENT '',
  `indirizzo` VARCHAR(45) NULL COMMENT '',
  `telefono` VARCHAR(45) NULL COMMENT '',
  `mail` VARCHAR(45) NULL COMMENT '',
  `tesseraNr` INT(11) NULL COMMENT '',
  PRIMARY KEY (`idPersona`)  COMMENT '',
  INDEX `nome` (`nome` ASC)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `booking`.`Campo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`Campo` (
  `idCampo` INT NOT NULL COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  `aperturaOra` INT NOT NULL DEFAULT 8 COMMENT '',
  `aperturaMin` INT NOT NULL DEFAULT 0 COMMENT '',
  `chiusuraOra` INT NOT NULL DEFAULT 20 COMMENT '',
  `inervalloOra` INT NOT NULL DEFAULT 13 COMMENT '',
  `intervalloOre` INT NOT NULL DEFAULT 2 COMMENT '',
  PRIMARY KEY (`idCampo`)  COMMENT '',
  INDEX `nome` (`nome` ASC)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `booking`.`Preno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `booking`.`Preno` (
  `idPersona` INT NOT NULL COMMENT '',
  `idCampo` INT NOT NULL COMMENT '',
  `data` DATE NOT NULL COMMENT '',
  `oraInizio` INT NOT NULL COMMENT '',
  `nrOre` INT NOT NULL DEFAULT 1 COMMENT '',
  PRIMARY KEY (`idPersona`, `idCampo`)  COMMENT '',
  INDEX `fk_Preno_Campo_idx` (`idCampo` ASC)  COMMENT '',
  INDEX `fk_Preno_Persona_idx` (`idPersona` ASC)  COMMENT '',
  INDEX `data` (`data` ASC, `oraInizio` ASC)  COMMENT '',
  CONSTRAINT `fk_Persona_has_Campo_Persona`
    FOREIGN KEY (`idPersona`)
    REFERENCES `booking`.`Persona` (`idPersona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_has_Campo_Campo1`
    FOREIGN KEY (`idCampo`)
    REFERENCES `booking`.`Campo` (`idCampo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
