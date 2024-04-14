-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 10/04/2024 às 16:48
-- Versão do servidor: 10.4.28-MariaDB
-- Versão do PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `javadb`
--

DELIMITER $$
--
-- Procedimentos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `buscarComID` (IN `_consulta` INT)   BEGIN 
    	IF(_consulta IS NULL) THEN
        	SELECT cro.relative_id, vol.time
            FROM cronometro cro, volta vol
            WHERE cro.relative_id = vol.relative_id
            ORDER BY cro.time;
        ELSE
        	SELECT cro.relative_id, vol.time
            FROM cronometro cro, volta vol
            WHERE cro.relative_id = vol.relative_id
           	AND cro.relative_id LIKE CONCAT('%', _consulta ,'%');
        END IF;
    END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura para tabela `cronometro`
--

CREATE TABLE `cronometro` (
  `id` int(11) NOT NULL,
  `relative_id` int(11) NOT NULL,
  `time` text NOT NULL COMMENT 'tempo total sendo H | M | S | MS'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `volta`
--

CREATE TABLE `volta` (
  `id` int(11) NOT NULL,
  `relative_id` int(11) NOT NULL,
  `time` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `cronometro`
--
ALTER TABLE `cronometro`
  ADD PRIMARY KEY (`id`),
  ADD KEY `relative_id` (`relative_id`);

--
-- Índices de tabela `volta`
--
ALTER TABLE `volta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `relative_id` (`relative_id`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `cronometro`
--
ALTER TABLE `cronometro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `volta`
--
ALTER TABLE `volta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `cronometro`
--
ALTER TABLE `cronometro`
  ADD CONSTRAINT `fk_relativeID` FOREIGN KEY (`relative_id`) REFERENCES `volta` (`relative_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
