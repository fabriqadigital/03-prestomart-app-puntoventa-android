/*
 Navicat Premium Data Transfer

 Source Server         : LocalHost
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : bd_ecommerce

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 14/07/2026 10:47:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administracion_cliente
-- ----------------------------
DROP TABLE IF EXISTS `administracion_cliente`;
CREATE TABLE `administracion_cliente`  (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `apellido` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `telefono` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ruc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `razon_social` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `observacion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `fecha_nacimiento` date NULL DEFAULT NULL,
  `ubigueo_pais` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ubigueo_departamento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ubigueo_provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ubigueo_distrito` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `codigo_nip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT 'Estado: S = Activo, N = Inactivo',
  `id_usuario` int NULL DEFAULT NULL,
  `tipo_cliente` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tipos de clientes: \r\n1 = Clientes administrado,\r\n2= Clientes Normales (Usuario N), \r\n3= Clientes LP (Usuario LP)',
  `genero` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_cliente`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_cliente
-- ----------------------------
INSERT INTO `administracion_cliente` VALUES (1, 'Juan Carlos', 'Pérez Gómez', '987654321', 'juanperez89@gmail.com', 'Av. Perú 245, Urb. Los Pinos', NULL, NULL, '2024-05-12 10:15:00', '2025-11-20 14:30:00', 'S', NULL, '1989-03-15', 'PE', 'Lima', 'Lima', 'San Martín de Porres', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (2, 'María Elena', 'Torres Ruiz', '956123789', 'maria.torres.r@gmail.com', 'Jr. Huaraz 780', NULL, NULL, '2024-06-08 09:45:00', '2025-12-05 11:20:00', 'S', 'Cliente frecuente, siempre compra leche y pan', '1992-07-22', 'PE', 'Lima', 'Lima', 'Comas', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (3, 'José Antonio', 'Ramírez López', '991234567', NULL, 'Calle Los Jazmines 123, Ate Vitarte', NULL, NULL, '2024-03-20 14:10:00', '2025-10-15 16:55:00', 'S', NULL, '1978-11-05', 'PE', 'Lima', 'Lima', 'Ate', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (4, 'Rosa Mercedes', 'Vásquez Díaz', '934567890', 'rosavazquez22@hotmail.com', 'Av. Alfredo Mendiola 456', NULL, NULL, '2025-01-10 08:30:00', '2025-12-01 09:15:00', 'S', 'Prefiere pagar en efectivo', '1995-04-18', 'PE', 'Lima', 'Lima', 'Independencia', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (5, 'Carlos Eduardo', 'Mendoza Castro', '999888777', 'carlosmendoza77@gmail.com', 'Pasaje Las Flores s/n, Villa El Salvador', NULL, NULL, '2024-09-05 11:00:00', '2025-11-28 13:40:00', 'S', NULL, '1985-09-30', 'PE', 'Lima', 'Lima', 'Villa El Salvador', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (6, 'Ana Patricia', 'García Soto', '987001234', 'ana.garcia@tiendaelbuenpreciosac.com', 'Av. La Marina 2500, Pueblo Libre', '20567890123', 'TIENDA EL BUEN PRECIO E.I.R.L.', '2024-04-15 15:20:00', '2025-10-30 10:45:00', 'S', 'Cliente corporativo, factura mensual', '1980-12-10', 'PE', 'Lima', 'Lima', 'Pueblo Libre', 'CL001', 'N', 5, '1', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (7, 'Luis Fernando', 'Quispe Huamán', '966554433', NULL, 'Calle Las Dalias 89, Los Olivos', '10456789012', 'LUIS F. QUISPE H. - SERVICIOS GENERALES', '2025-02-18 09:10:00', '2025-11-10 12:25:00', 'S', 'Pide factura electrónica', '1990-06-14', 'PE', 'Lima', 'Lima', 'Los Olivos', 'LFQH-001', 'N', NULL, '3', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (8, 'Carmen Rosa', 'Flores Medina', '912345678', 'carmenfloresventas@gmail.com', 'Av. Tupac Amaru 1450, San Juan de Miraflores', NULL, NULL, '2024-07-22 13:50:00', '2025-12-10 15:00:00', 'S', NULL, '1987-02-28', 'PE', 'Lima', 'Lima', 'San Juan de Miraflores', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (9, 'Pedro Pablo', 'Sánchez Vargas', '955667788', 'pedrosanchez84@outlook.com', 'Jr. Amazonas 320, Cercado de Lima', '20123456789', 'SÁNCHEZ VARGAS PEDRO PABLO', '2024-11-01 10:05:00', '2025-09-20 14:10:00', 'S', 'Cliente LP - descuentos por volumen', '1984-08-19', 'PE', 'Lima', 'Lima', 'Cercado de Lima', 'PP-SV-2024', 'N', 12, '3', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (10, 'Karla Andrea', 'Morales Chávez', '944332211', 'karlamorales95@yahoo.es', 'Urb. Las Palmeras Mz. B Lt. 15, Puente Piedra', NULL, NULL, '2025-03-05 16:40:00', '2025-11-25 09:30:00', 'S', 'Compra snacks para sus hijos', '1995-10-03', 'PE', 'Lima', 'Lima', 'Puente Piedra', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (11, 'Miguel Ángel', 'Cruz Rivera', '977889900', NULL, 'Av. Perú 890, Callao', NULL, NULL, '2024-08-14 11:25:00', '2025-10-05 17:15:00', 'S', NULL, '1975-01-25', 'PE', 'Lima', 'Lima', 'Callao', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (12, 'Sonia Elizabeth', 'Rojas Guzmán', '988776655', 'soniarojas_81@gmail.com', 'Calle Los Pinos 567, Surquillo', NULL, NULL, '2024-12-20 08:55:00', '2025-12-15 10:20:00', 'S', 'Cliente nuevo, muy amable', '1981-05-12', 'PE', 'Lima', 'Lima', 'Surquillo', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (13, 'Raúl Humberto', 'Ortiz Mendoza', '933221144', 'raulortiz77@hotmail.com', 'Av. Javier Prado Este 3200, La Molina', '20678901234', 'IMPORTACIONES RAUL O. E.I.R.L.', '2025-04-10 14:00:00', '2025-11-18 11:50:00', 'S', 'Factura por compras grandes', '1977-09-08', 'PE', 'Lima', 'Lima', 'La Molina', 'RO-IMP-001', 'N', 8, '1', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (14, 'Patricia', 'Luyo Fernández', '966554422', NULL, 'Jr. Ayacucho 450, Breña', NULL, NULL, '2024-10-30 09:35:00', '2025-10-22 13:10:00', 'S', NULL, '1993-03-27', 'PE', 'Lima', 'Lima', 'Breña', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (15, 'Víctor Manuel', 'Herrera Soto', '955443322', 'victorhsoto@gmail.com', 'Av. Colonial 1234, Rímac', NULL, NULL, '2025-01-15 12:20:00', '2025-12-08 15:45:00', 'S', 'Siempre pide cigarros sueltos', '1988-12-01', 'PE', 'Lima', 'Lima', 'Rímac', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (16, 'Liliana', 'Campos Vargas', '944556677', 'lilianacamposv@gmail.com', 'Urb. Santa Rosa Mz. K Lt. 8, Villa María del Triunfo', NULL, NULL, '2024-02-28 10:50:00', '2025-11-05 16:30:00', 'S', NULL, '1990-07-14', 'PE', 'Lima', 'Lima', 'Villa María del Triunfo', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (17, 'Edwin', 'Palomino Ruiz', '977112233', NULL, 'Calle Los Libertadores 890, San Borja', '10432109876', 'EDWIN PALOMINO R. - CONSULTOR', '2025-05-20 15:15:00', '2025-12-12 09:00:00', 'S', 'Cliente LP, descuentos especiales', '1986-04-09', 'PE', 'Lima', 'Lima', 'San Borja', 'EP-R-2025', 'N', 15, '3', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (18, 'Gloria', 'Montes de Oca', '988990011', 'gloriamontes@gmail.com', 'Av. Angamos Este 1800, Surco', NULL, NULL, '2024-07-10 13:40:00', '2025-10-28 14:20:00', 'S', NULL, '1982-11-20', 'PE', 'Lima', 'Lima', 'Santiago de Surco', NULL, 'N', NULL, '2', 'F', NULL);
INSERT INTO `administracion_cliente` VALUES (19, 'Fernando', 'Valdivia Ponce', '933445566', 'fernandovaldivia@hotmail.com', 'Jr. Iquitos 567, Jesús María', NULL, NULL, '2025-03-25 11:10:00', '2025-11-30 10:55:00', 'S', 'Compra diario por las mañanas', '1979-02-17', 'PE', 'Lima', 'Lima', 'Jesús María', NULL, 'N', NULL, '2', 'M', NULL);
INSERT INTO `administracion_cliente` VALUES (20, 'Verónica', 'Espinoza Díaz', '966778899', NULL, 'Pasaje Los Rosales 45, Miraflores', NULL, NULL, '2024-09-18 09:25:00', '2025-12-03 12:35:00', 'S', NULL, '1994-08-05', 'PE', 'Lima', 'Lima', 'Miraflores', NULL, 'N', NULL, '2', 'F', NULL);

-- ----------------------------
-- Table structure for administracion_compania
-- ----------------------------
DROP TABLE IF EXISTS `administracion_compania`;
CREATE TABLE `administracion_compania`  (
  `id_compania` int NOT NULL AUTO_INCREMENT,
  `ruc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `razon_social` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nombre_comercial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `logo_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sol_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sol_pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cert_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_id` int NULL DEFAULT NULL,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `production` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_ubigueo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_departamento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_distrito` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_urbanizacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address_codigo_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ref_direccion_referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ref_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ref_telefono_movil` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ref_telefono_fijo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `serie_guia_remision` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'debe empezar con la letra \"T\". Los siguientes tres caracteres pueden ser numéricos o alfanuméricos (ej. T001, TAB1)',
  PRIMARY KEY (`id_compania`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_compania
-- ----------------------------
INSERT INTO `administracion_compania` VALUES (1, '20607965987', 'INNOVACION PARA EL CONCRETO S.A.C.', '\r\nINNOVACION PARA EL CONCRETO', 'logos/Y1tjK8ZitNWPSVyw8VShvXnTmaSug1vAsrDGxxi4.jpg', 'prueba', 'prueba', 'certs/O8W9ZDgDRfHFtucX6hmewIr8mkZeqHaNQIZaGnGL.txt', 1, '123456789', '0', '1', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', '-', 'PARCELA NRO 4 HAB. PREURBANA NIEVERIA 2 ', '0000', 'A UNA CDRA\r\n\r\nCASETA DE SERENAZGO NIEVERIA', 'ventas@acconcretos.com.pe', '995-636-232', '(01) 680-5405', '2025-04-10 00:06:33', '2025-04-10 00:06:42', 'S', 'T001');

-- ----------------------------
-- Table structure for administracion_comprobante
-- ----------------------------
DROP TABLE IF EXISTS `administracion_comprobante`;
CREATE TABLE `administracion_comprobante`  (
  `id_comprobante` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_comprobante`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_comprobante
-- ----------------------------
INSERT INTO `administracion_comprobante` VALUES (1, 'BOLETA', '2025-01-13 21:00:44', '2025-01-13 21:00:44', 'S');
INSERT INTO `administracion_comprobante` VALUES (2, 'FACTURA', '2025-01-13 21:00:44', '2025-01-13 21:00:44', 'S');
INSERT INTO `administracion_comprobante` VALUES (3, 'RECIBO', '2025-01-13 21:00:44', '2025-01-13 21:00:44', 'S');

-- ----------------------------
-- Table structure for administracion_empleado
-- ----------------------------
DROP TABLE IF EXISTS `administracion_empleado`;
CREATE TABLE `administracion_empleado`  (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `apellido` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `telefono` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dni` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `id_usuario` int NULL DEFAULT NULL,
  `licencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_empleado`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of administracion_empleado
-- ----------------------------
INSERT INTO `administracion_empleado` VALUES (1, 'LUCERO', 'TERRONES SALCEDO', '9959636232', NULL, NULL, '70081032', '2025-03-18 10:10:59', '2025-03-18 10:10:59', 'S', 28, NULL);
INSERT INTO `administracion_empleado` VALUES (2, 'DEBORA', 'CHAUPIN LAIME', '983529737', 'CHAUPINLAIMEDEBORA@GMAIL.COM', NULL, '70993025', '2025-03-18 10:28:28', '2025-03-18 10:28:28', 'S', 29, NULL);
INSERT INTO `administracion_empleado` VALUES (3, 'ESTEFANY', 'ARROYO RAMOS', '978648479', NULL, NULL, '71441000', '2025-03-18 10:32:26', '2025-03-18 10:32:26', 'S', 30, NULL);
INSERT INTO `administracion_empleado` VALUES (4, 'JAVIER', 'PUTPANA SANGAMA', '900603701', NULL, NULL, '75227587', '2025-03-18 10:33:52', '2025-03-18 10:33:52', 'S', 31, NULL);
INSERT INTO `administracion_empleado` VALUES (5, 'JEREMIAS', 'CASTILLO BELTRAN', '917313025', NULL, NULL, '75845780', '2025-03-18 10:35:08', '2025-07-30 14:46:12', 'S', 32, NULL);
INSERT INTO `administracion_empleado` VALUES (6, 'EDUARDO', 'CALLUPE PAUCAR', '913165980', NULL, NULL, '76555000', '2025-07-01 19:17:13', '2025-07-01 19:17:13', 'S', 0, 'Q76555000');
INSERT INTO `administracion_empleado` VALUES (7, 'JUAN DIEGO', 'MIRANDA SOTO', '910152831', NULL, NULL, '46276331', '2025-07-02 11:29:00', '2025-07-02 11:29:00', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (8, 'ABEL GERARDO', 'RIOS ENRIQUE', '953317013', NULL, 'MZ. A LT. 05 RESIDENCIAL LOS PORTALES DE CARAPONGA-SAN JUAN DE LURIGANCHO', '41027935', '2025-07-02 11:31:58', '2025-07-02 11:31:58', 'S', 0, 'Q41027935');
INSERT INTO `administracion_empleado` VALUES (10, 'DANIEL', 'HUANUQUEÑO', NULL, NULL, NULL, NULL, '2025-07-02 12:27:12', '2025-07-02 12:27:12', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (11, 'JULIO', 'ISLA ROJAS', '901149942', NULL, NULL, '41477780', '2025-07-02 12:31:23', '2025-07-02 18:38:53', 'S', 0, 'Q41477780');
INSERT INTO `administracion_empleado` VALUES (12, 'MIULER', 'PUTPAÑA VELA', '946322254', NULL, NULL, '00966079', '2025-07-02 12:33:33', '2025-07-02 18:38:30', 'S', 0, 'Q00966079');
INSERT INTO `administracion_empleado` VALUES (13, 'LUIS', 'CHAVEZ ANDIA', '914659103', NULL, NULL, '06580869', '2025-07-02 12:34:08', '2025-07-02 18:37:59', 'S', 0, 'A06580869');
INSERT INTO `administracion_empleado` VALUES (14, 'ELFERES', 'VENEGAS YAHUARA', '964319805', NULL, NULL, '43604086', '2025-07-02 12:35:01', '2025-07-02 18:37:30', 'S', 0, 'Q43604086');
INSERT INTO `administracion_empleado` VALUES (15, 'JHONATAN', 'RUIZ GALVEZ', '925640419', NULL, NULL, '45815981', '2025-07-02 12:35:35', '2025-07-02 18:37:02', 'S', 0, 'Q45815981');
INSERT INTO `administracion_empleado` VALUES (16, 'YONEL', 'ALVAREZ FALCON', '990268793', NULL, NULL, '44003455', '2025-07-02 12:36:35', '2025-07-02 18:36:25', 'S', 0, 'Q44003455');
INSERT INTO `administracion_empleado` VALUES (18, 'BRYAN', 'ARROYO', NULL, NULL, NULL, NULL, '2025-07-03 09:53:39', '2025-07-03 09:53:39', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (19, 'LENER', 'GUERRA', '978312620', NULL, NULL, '73959760', '2025-07-03 09:57:18', '2025-08-02 15:24:13', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (20, 'SAMIR', 'ZARATE SANTOS', '906022153', NULL, NULL, '70522869', '2025-07-03 16:04:31', '2025-08-02 15:26:08', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (21, 'LIMBER', 'QUISPE MATEO', NULL, NULL, NULL, '61253456', '2025-07-03 16:06:20', '2025-07-03 16:06:20', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (22, 'OWEN', 'URCO VEGA', '901289787', NULL, NULL, '10113244', '2025-07-03 16:08:16', '2025-11-06 18:13:19', 'S', 0, 'Y10113244');
INSERT INTO `administracion_empleado` VALUES (23, 'SERGIO', 'CAYMATA HUAYAMBAHUA', '904545863', NULL, NULL, '48861698', '2025-07-03 16:09:56', '2025-08-02 15:27:21', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (24, 'RODOLFO', 'PUTPAÑA VELA', '929527759', NULL, NULL, '00877487', '2025-07-03 16:11:33', '2025-08-02 15:26:45', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (25, 'VICTOR MIGUEL', 'TANCHIMA NATORCE', '932710378', NULL, NULL, '61101231', '2025-07-03 16:12:20', '2025-08-02 15:27:59', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (26, 'RAUL', 'ARROYO MONTALVAN', NULL, NULL, NULL, NULL, '2025-07-08 08:53:58', '2025-07-08 08:53:58', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (27, 'HECTOR', 'FERRER', NULL, NULL, NULL, NULL, '2025-07-08 09:04:19', '2025-07-08 09:04:19', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (28, 'ELVIS', 'CERDAN', NULL, NULL, NULL, NULL, '2025-07-08 09:06:07', '2025-07-08 09:06:07', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (29, 'JOSE', 'TORREZ COYAC', NULL, NULL, NULL, NULL, '2025-07-08 09:27:12', '2025-09-12 21:35:41', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (30, 'XIMENA', 'ABIERTO', NULL, NULL, NULL, NULL, '2025-07-08 12:02:28', '2025-07-08 12:02:28', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (31, 'DAYAN', 'ARROYO', NULL, NULL, NULL, NULL, '2025-07-08 14:48:13', '2025-07-08 14:48:13', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (32, 'ROBERTO', 'GUANIPA', NULL, NULL, NULL, NULL, '2025-07-09 10:34:40', '2025-07-09 14:34:58', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (33, 'FALCO', NULL, NULL, NULL, NULL, NULL, '2025-09-19 09:30:25', '2025-09-19 09:30:25', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (34, 'JOSE', 'TORRES COLLAC', NULL, NULL, NULL, NULL, '2025-10-01 12:15:53', '2025-10-01 12:15:53', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (35, 'JULIO RAUL', 'QUISPE ARONI', NULL, NULL, NULL, '40701485', '2025-10-17 13:36:46', '2025-10-17 17:37:27', 'S', 0, 'Q40701485');
INSERT INTO `administracion_empleado` VALUES (36, 'HECTOR', 'FERRER YSARBE', NULL, NULL, NULL, '10248157', '2025-10-29 08:29:15', '2025-10-29 08:29:15', 'S', 0, 'Q10248157');
INSERT INTO `administracion_empleado` VALUES (37, 'LIDMER', 'TANCHIMA', NULL, NULL, NULL, NULL, '2025-11-19 13:06:45', '2025-11-19 18:07:34', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (38, 'DILIO', 'TANCHIMA', NULL, NULL, NULL, NULL, '2025-11-19 13:08:18', '2025-11-19 13:08:18', 'S', 0, NULL);
INSERT INTO `administracion_empleado` VALUES (39, 'JACK HUGO', 'LOPEZ CONDOR', NULL, NULL, NULL, '73960091', '2025-11-25 08:53:59', '2025-11-25 08:53:59', 'S', 0, NULL);

-- ----------------------------
-- Table structure for administracion_forma_pago
-- ----------------------------
DROP TABLE IF EXISTS `administracion_forma_pago`;
CREATE TABLE `administracion_forma_pago`  (
  `id_forma_pago` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_forma_pago`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_forma_pago
-- ----------------------------
INSERT INTO `administracion_forma_pago` VALUES (1, 'CONTADO', '2025-01-13 21:00:44', '2025-01-13 21:00:44');
INSERT INTO `administracion_forma_pago` VALUES (2, 'CREDITO', '2025-01-13 21:00:44', '2025-01-13 21:00:44');

-- ----------------------------
-- Table structure for administracion_pedidos
-- ----------------------------
DROP TABLE IF EXISTS `administracion_pedidos`;
CREATE TABLE `administracion_pedidos`  (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NULL DEFAULT NULL,
  `id_vendedor` int NULL DEFAULT NULL,
  `id_forma_pago` int NULL DEFAULT NULL,
  `id_comprobante` int NULL DEFAULT NULL,
  `estado` int NULL DEFAULT 1 COMMENT 'estado: 1 = Pendiente , 2 = En proceso, 3 =Finalizado',
  `id_diseno` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `id_usuario` int NULL DEFAULT NULL,
  PRIMARY KEY (`id_pedido`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 953 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_pedidos
-- ----------------------------
INSERT INTO `administracion_pedidos` VALUES (2, 4, NULL, 2, 2, 2, 20, '2025-03-18 11:39:38', '2025-07-02 12:36:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (3, 4, NULL, 2, 2, 1, 10, '2025-03-18 11:46:34', '2025-03-18 11:46:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (4, 3, NULL, 2, 2, 2, 20, '2025-03-18 11:49:56', '2025-03-19 11:07:31', 'S', 1);
INSERT INTO `administracion_pedidos` VALUES (5, 4, NULL, 2, 2, 1, 10, '2025-03-18 11:50:31', '2025-03-18 11:50:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (6, 4, NULL, 2, 2, 2, 20, '2025-03-18 11:51:19', '2025-03-19 11:07:17', 'S', 1);
INSERT INTO `administracion_pedidos` VALUES (7, 2, NULL, 1, 2, 2, 10, '2025-03-18 11:52:52', '2025-03-19 11:07:58', 'S', 1);
INSERT INTO `administracion_pedidos` VALUES (8, 5, NULL, 2, 2, 0, 12, '2025-03-19 10:18:13', '2025-03-19 12:51:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (9, 4, NULL, 2, 2, 0, 20, '2025-03-19 10:21:51', '2025-03-19 13:08:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (10, 4, NULL, 2, 2, 0, 10, '2025-03-19 10:24:11', '2025-03-19 13:51:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (11, 3, NULL, 2, 2, 0, 20, '2025-03-19 10:28:17', '2025-03-19 13:27:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (12, 2, NULL, 2, 2, 0, 10, '2025-03-19 10:39:54', '2025-03-19 18:16:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (13, 4, NULL, 2, 2, 0, 20, '2025-03-19 11:55:35', '2025-03-19 14:16:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (14, 3, NULL, 2, 2, 0, 20, '2025-03-19 17:50:06', '2025-03-20 13:25:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (34, 4, NULL, 2, 2, 0, 20, '2025-03-19 18:12:18', '2025-03-20 13:46:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (35, 6, NULL, 2, 2, 0, 12, '2025-03-19 18:36:49', '2025-03-20 12:18:42', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (36, 7, NULL, 2, 2, 0, 24, '2025-03-19 18:46:00', '2025-03-20 09:08:08', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (37, 2, NULL, 2, 2, 0, 10, '2025-03-20 08:42:03', '2025-03-20 17:15:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (38, 3, NULL, 2, 2, 0, 20, '2025-03-20 16:47:29', '2025-04-11 08:26:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (39, 4, NULL, 2, 2, 0, 20, '2025-03-20 17:05:22', '2025-03-21 13:45:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (40, 4, NULL, 2, 2, 0, 10, '2025-03-20 17:09:24', '2025-03-21 14:02:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (41, 8, NULL, 1, 2, 0, 20, '2025-03-21 16:37:59', '2025-03-22 14:05:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (42, NULL, 5, 1, 3, 0, 12, '2025-03-21 17:18:14', '2025-03-22 13:38:09', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (43, NULL, 1, 1, 3, 0, 12, '2025-03-21 18:48:10', '2025-03-22 09:20:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (44, NULL, 1, 1, 3, 0, 12, '2025-03-22 07:59:50', '2025-03-22 08:28:56', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (45, NULL, 1, 2, 3, 0, 10, '2025-03-22 08:28:21', '2025-03-22 09:09:31', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (47, NULL, 1, 1, 3, 0, 12, '2025-03-22 08:59:14', '2025-03-22 12:43:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (48, 4, NULL, 2, 2, 0, 20, '2025-03-22 14:02:50', '2025-03-24 12:50:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (49, 4, NULL, 2, 2, 0, 12, '2025-03-22 14:04:22', '2025-03-24 14:12:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (50, 3, NULL, 2, 2, 0, 20, '2025-03-24 10:09:36', '2025-03-24 14:34:35', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (51, 2, NULL, 2, 2, 0, 10, '2025-03-24 10:11:23', '2025-03-24 17:48:11', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (52, 4, NULL, 2, 2, 0, 20, '2025-03-24 16:59:43', '2025-03-25 13:38:31', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (53, 3, NULL, 2, 2, 0, 10, '2025-03-24 17:05:00', '2025-03-25 11:07:38', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (54, 3, NULL, 2, 2, 0, 25, '2025-03-24 17:06:52', '2025-03-25 15:06:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (55, 2, NULL, 2, 2, 0, 10, '2025-03-25 09:51:15', '2025-03-25 16:58:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (57, NULL, 6, 1, 3, 0, 12, '2025-03-25 16:14:50', '2025-03-25 16:38:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (58, 4, NULL, 2, 2, 0, 20, '2025-03-25 17:21:50', '2025-03-26 13:24:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (59, 4, NULL, 2, 2, 0, 10, '2025-03-25 17:24:45', '2025-03-26 13:45:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (60, 3, NULL, 2, 2, 0, 12, '2025-03-26 08:33:55', '2025-03-26 11:52:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (61, 3, NULL, 2, 2, 0, 20, '2025-03-26 16:04:46', '2025-03-27 13:08:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (62, 4, NULL, 2, 2, 0, 20, '2025-03-26 17:42:30', '2025-03-27 13:26:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (63, 4, NULL, 2, 2, 0, 12, '2025-03-26 17:45:11', '2025-03-27 13:49:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (66, 11, NULL, 1, 2, 0, 12, '2025-03-27 14:56:23', '2025-03-28 07:00:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (67, 2, NULL, 2, 2, 0, 2, '2025-03-27 15:04:59', '2025-03-27 17:16:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (68, 4, NULL, 2, 2, 0, 10, '2025-03-27 18:08:34', '2025-03-29 12:20:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (70, 3, NULL, 2, 2, 0, 20, '2025-03-27 18:12:28', '2025-03-28 12:57:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (71, 4, NULL, 2, 2, 0, 20, '2025-03-28 11:54:49', '2025-03-28 13:21:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (72, 2, NULL, 2, 2, 0, 26, '2025-03-28 12:19:04', '2025-03-28 17:46:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (73, NULL, 1, 1, 3, 0, 12, '2025-03-28 15:34:01', '2025-03-29 11:14:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (74, 1, NULL, 2, 2, 0, 6, '2025-03-28 18:51:18', '2025-03-29 08:35:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (75, 2, NULL, 2, 2, 0, 26, '2025-03-29 09:08:13', '2025-03-29 16:21:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (76, 3, NULL, 2, 2, 2, 20, '2025-03-29 10:44:49', '2025-04-01 11:21:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (79, 4, NULL, 2, 2, 0, 12, '2025-03-29 11:39:52', '2025-03-31 09:38:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (80, NULL, 5, 1, 3, 1, NULL, '2025-03-31 08:39:48', '2025-03-31 08:39:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (81, NULL, 3, 1, 3, 1, NULL, '2025-03-31 08:42:52', '2025-03-31 08:42:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (82, NULL, 3, 1, 3, 1, NULL, '2025-03-31 08:51:00', '2025-03-31 08:51:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (83, NULL, 3, 1, 3, 0, 27, '2025-03-31 08:53:26', '2025-03-31 09:06:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (84, NULL, 7, 1, 3, 0, 27, '2025-03-31 08:56:17', '2025-03-31 10:04:09', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (85, NULL, 7, 1, 3, 0, 27, '2025-03-31 08:59:31', '2025-03-31 09:07:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (86, 2, NULL, 2, 2, 2, 2, '2025-03-31 09:36:54', '2025-03-31 18:23:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (87, 2, NULL, 2, 2, 0, 12, '2025-03-31 09:38:50', '2025-03-31 18:50:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (88, 2, NULL, 2, 2, 0, 12, '2025-03-31 09:49:58', '2025-04-01 19:51:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (89, 4, NULL, 2, 2, 0, 20, '2025-03-31 18:00:52', '2025-04-02 09:01:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (90, 4, NULL, 2, 2, 0, 10, '2025-03-31 18:02:33', '2025-04-02 09:01:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (91, 4, NULL, 2, 2, 0, 12, '2025-03-31 18:03:49', '2025-04-02 09:01:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (92, 3, NULL, 2, 2, 0, 25, '2025-03-31 19:06:59', '2025-04-01 15:07:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (93, 3, NULL, 2, 2, 0, 20, '2025-03-31 19:07:57', '2025-04-01 14:27:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (95, 2, NULL, 2, 2, 0, 26, '2025-04-01 08:45:36', '2025-04-01 17:19:17', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (96, 2, NULL, 2, 2, 2, 12, '2025-04-01 09:12:06', '2025-04-01 19:01:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (97, 2, NULL, 2, 2, 0, 12, '2025-04-01 09:15:47', '2025-04-01 18:29:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (98, 8, NULL, 1, 2, 0, 25, '2025-04-01 09:50:39', '2025-04-01 14:49:24', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (100, 3, NULL, 2, 2, 0, 20, '2025-04-02 08:56:30', '2025-04-02 13:03:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (101, 2, NULL, 2, 2, 0, 2, '2025-04-02 09:00:41', '2025-04-03 14:23:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (102, 1, NULL, 2, 2, 0, 28, '2025-04-02 09:11:04', '2025-04-02 10:54:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (103, 2, NULL, 2, 2, 0, 2, '2025-04-02 11:21:07', '2025-04-02 20:07:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (104, 2, NULL, 2, 2, 2, 12, '2025-04-02 17:12:31', '2025-04-02 18:41:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (105, 4, NULL, 2, 2, 0, 12, '2025-04-02 17:59:42', '2025-04-03 14:44:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (106, 4, NULL, 2, 2, 0, 20, '2025-04-02 18:00:39', '2025-04-03 14:44:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (107, 4, NULL, 2, 2, 2, 10, '2025-04-02 18:01:36', '2025-04-03 14:44:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (110, 2, NULL, 2, 2, 0, 12, '2025-04-03 08:34:51', '2025-04-03 16:00:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (111, 2, NULL, 2, 2, 0, 12, '2025-04-03 08:36:10', '2025-04-03 18:34:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (112, NULL, 1, 1, 3, 0, 12, '2025-04-03 08:38:32', '2025-04-03 11:39:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (113, 1, NULL, 2, 2, 0, 28, '2025-04-03 10:23:51', '2025-04-03 15:17:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (114, 6, NULL, 2, 2, 0, 20, '2025-04-03 12:32:32', '2025-04-04 08:34:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (115, 3, NULL, 2, 2, 0, 20, '2025-04-03 17:06:59', '2025-04-04 12:04:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (116, 4, NULL, 2, 2, 0, 10, '2025-04-03 17:20:58', '2025-04-04 15:04:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (117, 4, NULL, 2, 2, 0, 29, '2025-04-03 17:21:52', '2025-04-04 13:02:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (118, 4, NULL, 2, 2, 0, 12, '2025-04-03 17:23:00', '2025-04-04 14:01:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (119, 2, NULL, 2, 2, 0, 12, '2025-04-04 08:30:28', '2025-04-04 16:04:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (120, 2, NULL, 2, 2, 2, 12, '2025-04-04 08:31:26', '2025-04-07 12:28:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (121, 2, NULL, 2, 2, 0, 12, '2025-04-04 08:33:02', '2025-04-04 16:32:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (122, 12, NULL, 1, 3, 0, 30, '2025-04-04 12:11:34', '2025-04-05 13:20:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (123, 4, NULL, 2, 2, 0, 12, '2025-04-05 11:21:44', '2025-04-07 15:42:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (124, 4, NULL, 2, 2, 2, 18, '2025-04-05 11:23:41', '2025-04-07 15:42:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (125, 4, NULL, 2, 2, 2, 10, '2025-04-05 11:24:47', '2025-04-07 15:42:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (126, 3, NULL, 2, 2, 0, 20, '2025-04-05 11:26:00', '2025-04-08 09:29:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (127, 3, NULL, 2, 2, 0, 25, '2025-04-07 10:32:21', '2025-04-07 11:04:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (128, 2, NULL, 2, 2, 0, 2, '2025-04-07 12:19:29', '2025-04-08 08:44:24', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (129, 2, NULL, 2, 2, 1, 12, '2025-04-07 12:20:45', '2025-04-07 16:48:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (130, 2, NULL, 2, 2, 2, 12, '2025-04-07 12:21:57', '2025-04-07 19:08:24', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (132, 4, NULL, 2, 2, 0, 12, '2025-04-07 17:07:23', '2025-04-08 15:56:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (133, 4, NULL, 2, 2, 0, 10, '2025-04-07 17:08:16', '2025-04-08 13:53:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (134, 4, NULL, 2, 2, 0, 18, '2025-04-07 17:09:25', '2025-04-08 12:48:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (135, 3, NULL, 2, 2, 0, 25, '2025-04-07 18:00:10', '2025-04-08 11:35:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (136, 3, NULL, 2, 2, 0, 20, '2025-04-07 18:02:13', '2025-04-08 14:20:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (138, 2, NULL, 2, 2, 0, 2, '2025-04-08 11:57:06', '2025-04-09 12:58:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (139, 2, NULL, 2, 2, 0, 12, '2025-04-08 11:58:10', '2025-04-08 18:27:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (140, 2, NULL, 2, 2, 0, 12, '2025-04-08 11:59:04', '2025-04-08 18:42:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (141, 1, NULL, 2, 2, 0, 28, '2025-04-08 16:36:55', '2025-04-09 07:29:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (142, 3, NULL, 2, 2, 0, 25, '2025-04-08 18:34:58', '2025-04-09 11:59:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (143, 3, NULL, 2, 2, 0, 20, '2025-04-08 18:35:56', '2025-04-09 12:42:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (144, 2, NULL, 2, 2, 0, 2, '2025-04-09 11:40:22', '2025-04-09 16:39:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (145, 2, NULL, 2, 2, 0, 30, '2025-04-09 11:41:19', '2025-04-09 17:23:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (146, 2, NULL, 2, 2, 0, 30, '2025-04-09 11:42:10', '2025-04-09 17:04:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (147, 4, NULL, 2, 2, 2, 18, '2025-04-09 16:41:26', '2025-04-10 11:54:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (148, 4, NULL, 2, 2, 0, 10, '2025-04-09 16:42:26', '2025-04-10 12:54:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (149, 4, NULL, 2, 2, 0, 12, '2025-04-09 16:43:15', '2025-04-10 14:21:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (151, 2, NULL, 2, 2, 0, 30, '2025-04-09 18:37:05', '2025-04-10 17:14:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (153, 3, NULL, 2, 2, 2, 20, '2025-04-10 09:39:55', '2025-04-10 14:18:09', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (154, 2, NULL, 2, 2, 0, 30, '2025-04-10 14:18:26', '2025-04-11 07:30:12', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (155, 2, NULL, 2, 2, 0, 31, '2025-04-10 16:09:59', '2025-04-11 11:16:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (156, 2, NULL, 2, 2, 0, 30, '2025-04-10 16:16:28', '2025-04-15 09:30:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (157, 2, NULL, 2, 2, 0, 30, '2025-04-10 16:17:15', '2025-04-11 18:38:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (158, 4, NULL, 2, 2, 0, 20, '2025-04-10 17:49:31', '2025-04-11 12:29:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (160, 4, NULL, 2, 2, 0, 12, '2025-04-10 17:51:32', '2025-04-11 14:20:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (161, NULL, 1, 2, 3, 0, 12, '2025-04-10 18:18:10', '2025-04-11 09:22:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (162, 3, NULL, 2, 2, 1, NULL, '2025-04-11 08:25:45', '2025-04-11 08:25:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (163, 4, NULL, 2, 2, 0, 10, '2025-04-11 08:32:36', '2025-04-11 13:25:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (164, 3, NULL, 2, 2, 0, 25, '2025-04-11 08:40:59', '2025-04-11 12:11:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (165, 8, NULL, 1, 2, 0, 29, '2025-04-11 09:39:59', '2025-04-12 10:38:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (166, 3, NULL, 2, 2, 0, 20, '2025-04-11 09:58:58', '2025-04-11 12:49:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (167, 3, NULL, NULL, NULL, 0, 20, '2025-04-11 11:28:32', '2025-04-11 14:07:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (168, NULL, 2, 1, 3, 0, 30, '2025-04-11 12:04:23', '2025-04-11 18:17:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (169, 1, NULL, 2, 2, 0, 28, '2025-04-11 12:53:45', '2025-04-12 07:55:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (170, NULL, 1, 1, 3, 2, 30, '2025-04-11 18:51:19', '2025-04-16 17:09:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (171, 4, NULL, 2, 2, 0, 12, '2025-04-12 10:33:26', '2025-04-14 14:25:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (172, 4, NULL, 2, 2, 0, 10, '2025-04-12 10:34:11', '2025-04-14 14:04:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (173, 4, NULL, 2, 2, 0, 20, '2025-04-12 10:34:58', '2025-04-14 12:35:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (174, NULL, 3, 1, 3, 0, 30, '2025-04-12 10:57:12', '2025-04-12 11:19:23', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (176, 3, NULL, 2, 2, 0, 25, '2025-04-14 10:53:42', '2025-04-14 11:28:39', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (177, 3, NULL, 2, 2, 0, 12, '2025-04-14 10:55:10', '2025-04-14 13:05:06', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (178, 2, NULL, 2, 2, 0, 30, '2025-04-14 16:09:14', '2025-04-14 16:38:40', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (179, 2, NULL, 2, 2, 0, 30, '2025-04-14 16:10:44', '2025-04-14 17:52:00', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (180, 2, NULL, 2, 2, 0, 30, '2025-04-15 09:32:33', '2025-04-15 16:29:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (181, 2, NULL, 2, 2, 0, 30, '2025-04-15 09:35:46', '2025-04-15 15:43:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (182, 3, NULL, 2, 2, 2, 25, '2025-04-15 09:38:45', '2025-04-15 13:26:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (183, 3, NULL, 2, 2, 0, 20, '2025-04-15 09:40:32', '2025-04-15 12:41:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (184, 4, NULL, 2, 2, 0, 20, '2025-04-15 12:04:05', '2025-04-15 12:23:16', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (186, 4, NULL, 2, 2, 0, 10, '2025-04-15 12:05:25', '2025-04-15 13:04:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (187, 4, NULL, 2, 2, 0, 12, '2025-04-15 12:06:22', '2025-04-15 13:46:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (189, 3, NULL, 2, 2, 0, 31, '2025-04-15 18:02:36', '2025-04-16 07:10:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (190, 3, NULL, 2, 2, 2, 25, '2025-04-15 18:03:51', '2025-04-16 12:52:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (191, 3, NULL, 2, 2, 0, 20, '2025-04-15 18:06:06', '2025-04-16 17:08:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (192, 4, NULL, 2, 2, 0, 10, '2025-04-15 18:07:39', '2025-04-16 14:15:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (193, 13, NULL, 1, 2, 0, 25, '2025-04-15 18:15:46', '2025-04-16 11:45:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (194, 2, NULL, 2, 2, 0, 30, '2025-04-16 05:58:36', '2025-04-16 06:48:56', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (195, 2, NULL, 2, 2, 1, 12, '2025-04-16 08:48:43', '2025-04-16 17:09:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (196, 2, NULL, 2, 2, 0, 30, '2025-04-16 08:49:51', '2025-04-16 18:05:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (197, 4, NULL, 2, 2, 0, 12, '2025-04-16 09:40:42', '2025-04-16 13:06:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (198, 14, NULL, 2, 2, 0, 30, '2025-04-16 17:02:13', '2025-04-21 18:37:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (199, NULL, 7, 1, 3, 0, 30, '2025-04-17 08:24:39', '2025-04-17 08:56:12', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (200, NULL, 7, 1, 3, 0, 6, '2025-04-17 09:12:55', '2025-04-17 12:11:10', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (201, NULL, 1, 1, 3, 0, 30, '2025-04-19 09:23:21', '2025-04-25 12:02:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (202, 4, NULL, 2, 2, 0, 20, '2025-04-19 11:43:25', '2025-04-21 12:46:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (203, 4, NULL, 2, 2, 0, 10, '2025-04-19 11:44:22', '2025-04-21 13:02:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (204, 4, NULL, 2, 2, 0, 12, '2025-04-19 11:45:15', '2025-04-21 14:28:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (206, 3, NULL, 2, 2, 0, 20, '2025-04-21 08:44:22', '2025-04-21 12:29:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (207, 3, NULL, 2, 2, 0, 25, '2025-04-21 09:24:29', '2025-04-21 14:12:08', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (208, 2, NULL, 2, 2, 0, 30, '2025-04-21 10:14:49', '2025-04-21 18:18:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (209, 15, NULL, 1, 3, 0, 30, '2025-04-21 11:47:56', '2025-04-22 09:12:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (210, 2, NULL, 2, 2, 0, 30, '2025-04-21 14:31:24', '2025-04-22 16:56:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (211, NULL, 7, 1, 3, 0, 6, '2025-04-22 07:48:14', '2025-04-22 08:17:49', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (212, 4, NULL, 2, 2, 0, 20, '2025-04-22 08:51:24', '2025-04-22 12:08:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (213, 4, NULL, 2, 2, 0, 10, '2025-04-22 08:52:37', '2025-04-22 12:52:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (214, 4, NULL, 2, 2, 0, 12, '2025-04-22 08:53:24', '2025-04-22 13:44:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (215, 3, NULL, 2, 2, 0, 12, '2025-04-22 08:55:21', '2025-04-22 12:31:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (216, 3, NULL, 2, 2, 0, 25, '2025-04-22 08:56:03', '2025-04-22 11:53:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (217, 3, NULL, 2, 2, 0, 25, '2025-04-22 08:57:53', '2025-04-22 13:28:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (218, 3, NULL, 2, 2, 0, 20, '2025-04-22 10:03:06', '2025-04-22 14:05:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (219, 2, NULL, 2, 2, 0, 30, '2025-04-22 10:13:53', '2025-04-23 16:57:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (220, 4, NULL, 2, 2, 0, 20, '2025-04-22 16:53:29', '2025-04-23 12:26:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (221, 4, NULL, 2, 2, 0, 32, '2025-04-22 16:54:24', '2025-04-23 13:15:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (222, 4, NULL, 2, 2, 0, 12, '2025-04-22 16:55:07', '2025-04-23 13:27:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (223, 3, NULL, 2, 2, 0, 25, '2025-04-23 09:55:55', '2025-04-23 12:34:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (224, 3, NULL, 2, 2, 0, 12, '2025-04-23 09:56:30', '2025-04-23 12:34:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (225, 3, NULL, 2, 2, 0, 12, '2025-04-23 09:57:08', '2025-04-23 12:47:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (226, 3, NULL, 2, 2, 0, 25, '2025-04-23 09:57:41', '2025-04-23 13:44:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (227, 3, NULL, 2, 2, 0, 20, '2025-04-23 09:58:19', '2025-04-23 14:07:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (229, 1, NULL, 2, 2, 0, 28, '2025-04-23 10:05:18', '2025-04-24 08:06:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (231, 4, NULL, 2, 2, 0, 10, '2025-04-23 17:32:59', '2025-04-24 13:44:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (232, 4, NULL, 2, 2, 0, 12, '2025-04-23 17:33:47', '2025-04-24 13:58:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (233, 4, NULL, 2, 2, 0, 20, '2025-04-23 17:34:56', '2025-04-24 12:16:19', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (234, 3, NULL, 2, 2, 0, 12, '2025-04-24 09:34:23', '2025-04-24 11:44:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (235, 3, NULL, 2, 2, 0, 25, '2025-04-24 09:34:58', '2025-04-24 12:42:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (236, 3, NULL, 2, 2, 0, 12, '2025-04-24 09:35:42', '2025-04-24 13:23:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (237, 3, NULL, 2, 2, 0, 25, '2025-04-24 09:36:43', '2025-04-24 15:53:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (239, 2, NULL, 2, 2, 0, 30, '2025-04-24 15:24:08', '2025-04-24 17:04:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (240, 4, NULL, 2, 2, 0, 20, '2025-04-24 17:52:26', '2025-04-25 12:25:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (241, 4, NULL, 2, 2, 0, 10, '2025-04-24 17:53:34', '2025-04-25 12:49:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (242, 4, NULL, 2, 2, 0, 12, '2025-04-24 17:54:44', '2025-04-25 13:40:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (243, 2, NULL, 2, 2, 0, 30, '2025-04-25 11:07:40', '2025-04-25 16:44:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (244, NULL, NULL, 1, 3, 0, 30, '2025-04-25 11:37:11', '2025-04-29 10:48:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (245, 16, NULL, 1, 3, 0, 30, '2025-04-25 11:39:52', '2025-04-25 15:14:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (246, 17, NULL, NULL, NULL, 0, 27, '2025-04-26 08:26:58', '2025-04-26 12:15:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (247, 17, NULL, NULL, NULL, 0, 27, '2025-04-26 08:27:37', '2025-04-26 12:15:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (248, NULL, NULL, 1, 3, 0, 30, '2025-04-26 08:39:35', '2025-04-26 16:30:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (249, NULL, 1, 1, 3, 0, 30, '2025-04-26 08:50:05', '2025-04-26 12:18:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (250, 18, NULL, 2, 2, 0, 18, '2025-04-26 09:32:30', '2025-04-29 12:30:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (251, 4, NULL, 2, 2, 2, 20, '2025-04-26 11:43:55', '2025-04-28 12:17:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (252, 4, NULL, 2, 2, 0, 10, '2025-04-26 11:44:40', '2025-04-28 13:14:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (253, 4, NULL, 2, 2, 0, 12, '2025-04-26 11:45:23', '2025-04-28 14:11:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (254, 3, NULL, 2, 2, 0, 25, '2025-04-28 09:15:56', '2025-04-28 12:40:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (255, 3, NULL, 2, 2, 0, 12, '2025-04-28 09:16:33', '2025-04-28 13:58:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (256, 3, NULL, 2, 2, 0, 25, '2025-04-28 09:17:06', '2025-04-28 14:51:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (257, 3, NULL, 2, 2, 0, 20, '2025-04-28 09:17:47', '2025-04-28 16:36:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (259, NULL, 7, NULL, 3, 0, 30, '2025-04-28 17:34:57', '2025-04-28 17:59:43', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (260, 4, NULL, 2, 2, 0, 20, '2025-04-28 17:35:05', '2025-04-29 12:47:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (261, 4, NULL, 2, 2, 0, 10, '2025-04-28 17:36:13', '2025-04-29 13:30:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (262, 4, NULL, 2, 2, 0, 12, '2025-04-28 17:37:14', '2025-04-29 14:38:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (263, 18, NULL, 2, 2, 0, 18, '2025-04-28 17:41:42', '2025-05-21 13:45:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (264, 1, NULL, 2, 2, 0, 28, '2025-04-28 17:57:27', '2025-04-29 07:45:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (265, 7, NULL, 2, 2, 0, 12, '2025-04-28 18:04:16', '2025-04-29 07:09:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (266, NULL, 7, 1, 3, 0, 6, '2025-04-29 07:58:34', '2025-04-29 08:25:25', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (267, 3, NULL, 2, 2, 0, 20, '2025-04-29 08:17:40', '2025-04-29 09:14:10', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (268, 3, NULL, 2, 2, 0, 25, '2025-04-29 08:29:25', '2025-04-29 14:21:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (269, 3, NULL, 2, 2, 0, 12, '2025-04-29 08:30:21', '2025-04-29 13:08:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (270, 3, NULL, 2, NULL, 0, 25, '2025-04-29 08:30:55', '2025-04-29 12:21:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (273, NULL, 7, 2, 3, 0, 6, '2025-04-29 15:42:24', '2025-04-29 16:15:02', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (275, 3, NULL, 2, 2, 0, 25, '2025-04-29 17:11:31', '2025-04-30 11:59:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (276, 4, NULL, 2, 2, 0, 10, '2025-04-29 17:12:13', '2025-04-30 13:21:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (277, 4, NULL, 2, 2, 0, 10, '2025-04-29 17:13:22', '2025-04-30 14:27:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (278, NULL, 7, 2, 3, 0, 30, '2025-04-30 08:34:26', '2025-04-30 09:04:17', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (279, NULL, 1, 1, 3, 0, 30, '2025-04-30 08:40:46', '2025-04-30 09:46:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (281, 3, NULL, 2, 2, 0, 20, '2025-04-30 08:57:45', '2025-04-30 14:07:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (283, 3, NULL, 2, 2, 0, 12, '2025-04-30 08:57:46', '2025-04-30 15:07:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (285, 1, NULL, 2, 2, 2, 28, '2025-04-30 10:45:03', '2025-04-30 17:05:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (286, 4, NULL, 2, 2, 0, 12, '2025-04-30 11:25:13', '2025-04-30 14:46:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (287, 4, NULL, 2, 2, 0, 20, '2025-04-30 11:28:52', '2025-04-30 12:35:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (289, 18, NULL, 2, 2, 0, 18, '2025-04-30 11:35:17', '2025-06-30 09:09:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (290, NULL, 7, 2, 3, 0, 6, '2025-04-30 18:14:37', '2025-04-30 18:35:27', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (291, 3, NULL, 2, 2, 0, 25, '2025-05-02 08:28:27', '2025-05-02 09:31:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (292, 3, NULL, 2, 2, 0, 12, '2025-05-02 08:31:11', '2025-05-02 10:21:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (293, 3, NULL, 2, 2, 0, 25, '2025-05-02 08:32:08', '2025-05-02 10:48:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (294, 3, NULL, 2, 2, 0, 12, '2025-05-02 08:32:49', '2025-05-02 11:33:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (295, 3, NULL, 2, 2, 0, 12, '2025-05-02 08:33:26', '2025-05-02 14:43:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (296, NULL, 7, 2, 3, 0, 6, '2025-05-02 09:13:01', '2025-05-02 09:52:02', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (297, 4, NULL, 2, 2, 0, 20, '2025-05-02 09:18:53', '2025-05-02 12:14:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (298, 4, NULL, 2, 2, 0, 10, '2025-05-02 09:19:53', '2025-05-02 12:46:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (299, 4, NULL, 2, 2, 0, 12, '2025-05-02 09:20:42', '2025-05-02 13:55:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (300, 18, NULL, 2, 2, 2, 18, '2025-05-02 09:25:17', '2025-05-02 14:18:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (302, 3, NULL, 2, 2, 0, 25, '2025-05-05 08:42:04', '2025-05-05 11:59:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (303, 3, NULL, 2, 2, 0, 12, '2025-05-05 08:43:34', '2025-05-05 12:20:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (304, 3, NULL, 2, 2, 0, 25, '2025-05-05 08:45:58', '2025-05-05 13:34:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (306, 18, NULL, 2, 2, 2, 18, '2025-05-05 08:57:55', '2025-05-05 18:32:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (307, 4, NULL, 2, 2, 0, 10, '2025-05-05 08:59:23', '2025-05-05 13:54:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (308, 4, NULL, 2, 2, 0, 10, '2025-05-05 09:00:14', '2025-05-05 12:43:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (309, 4, NULL, 2, 2, 0, 10, '2025-05-05 09:01:21', '2025-05-05 14:13:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (310, 1, NULL, 2, 2, 1, 28, '2025-05-05 10:22:52', '2025-05-05 15:41:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (312, 18, NULL, 2, 2, 0, 18, '2025-05-05 18:27:42', '2025-05-06 14:40:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (314, 3, NULL, 2, 2, 0, 12, '2025-05-05 18:34:53', '2025-05-06 09:39:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (315, 3, NULL, 2, 2, 0, 25, '2025-05-05 18:35:59', '2025-05-06 12:24:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (316, 3, NULL, 2, 2, 0, 20, '2025-05-05 18:36:42', '2025-05-06 14:14:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (317, 4, NULL, 2, 2, 0, 10, '2025-05-06 12:18:39', '2025-05-06 12:43:54', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (318, 4, NULL, 2, 2, 0, 10, '2025-05-06 12:53:54', '2025-05-06 13:34:53', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (319, 4, NULL, 2, 2, 0, 10, '2025-05-06 13:32:26', '2025-05-06 13:52:35', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (320, 7, NULL, 2, 2, 0, 12, '2025-05-06 18:35:57', '2025-05-07 07:27:47', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (321, 18, NULL, 2, 2, 2, 18, '2025-05-06 18:38:56', '2025-05-07 13:48:10', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (322, 3, NULL, 2, 2, 0, 24, '2025-05-06 18:43:13', '2025-05-07 11:23:22', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (323, 3, NULL, 2, 2, 0, 10, '2025-05-06 18:46:55', '2025-05-07 11:44:55', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (324, 3, NULL, 2, 2, 0, 24, '2025-05-06 18:48:36', '2025-05-07 18:22:28', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (325, 20, NULL, 2, 2, 0, 6, '2025-05-06 18:52:07', '2025-05-07 07:48:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (326, 4, NULL, 2, 2, 0, 10, '2025-05-06 19:01:59', '2025-05-07 12:45:56', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (327, 4, NULL, 2, 2, 0, 10, '2025-05-06 19:03:23', '2025-05-07 14:01:13', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (328, 4, NULL, 2, 2, 0, 12, '2025-05-06 19:04:26', '2025-05-07 14:18:50', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (329, 1, NULL, 2, 2, 0, 28, '2025-05-07 12:34:13', '2025-05-08 08:04:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (330, 20, NULL, 2, 2, 0, 6, '2025-05-07 16:42:14', '2025-05-08 07:03:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (331, 18, NULL, 2, 2, 0, 18, '2025-05-07 16:44:31', '2025-05-08 10:47:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (332, 18, NULL, 2, 2, 0, 18, '2025-05-07 17:24:44', '2025-05-08 14:54:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (333, 4, NULL, 2, 2, 0, 10, '2025-05-07 17:42:45', '2025-05-08 12:30:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (334, 4, NULL, 2, 2, 0, 10, '2025-05-07 17:43:48', '2025-05-08 13:47:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (335, 4, NULL, 2, 2, 0, 12, '2025-05-07 17:44:44', '2025-05-08 14:16:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (337, 3, NULL, 2, 2, 0, 25, '2025-05-07 18:24:35', '2025-05-08 11:14:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (338, 3, NULL, 2, 2, 0, 12, '2025-05-07 18:25:53', '2025-05-08 11:55:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (339, 3, NULL, 2, 2, 0, 25, '2025-05-07 18:27:12', '2025-05-08 12:59:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (341, 3, NULL, 2, 2, 0, 25, '2025-05-09 09:13:16', '2025-05-09 11:22:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (342, 3, NULL, 2, 2, 0, 12, '2025-05-09 09:14:08', '2025-05-09 11:50:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (343, 3, NULL, 2, 2, 0, 25, '2025-05-09 09:15:35', '2025-05-09 12:50:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (344, 3, NULL, 2, 2, 0, 20, '2025-05-09 09:18:47', '2025-05-09 13:39:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (345, 4, NULL, 2, 2, 0, 10, '2025-05-09 09:37:12', '2025-05-09 13:04:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (346, 2, NULL, 2, 2, 0, 12, '2025-05-09 12:01:09', '2025-05-09 15:39:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (347, 3, NULL, NULL, NULL, 0, 27, '2025-05-09 12:34:57', '2025-09-09 16:59:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (348, 3, NULL, NULL, NULL, 0, 27, '2025-05-09 12:36:18', '2025-09-09 16:59:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (349, 2, NULL, 2, 2, 0, 12, '2025-05-10 08:40:14', '2025-05-10 14:35:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (350, 7, NULL, 2, 2, 0, 18, '2025-05-12 08:36:14', '2025-05-12 12:57:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (351, 4, NULL, 2, 2, 0, 10, '2025-05-12 08:46:11', '2025-05-12 13:41:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (353, 4, NULL, 2, 2, 0, 12, '2025-05-12 08:49:05', '2025-05-12 14:00:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (357, 7, NULL, 2, 2, 0, 10, '2025-05-12 17:35:23', '2025-05-13 14:08:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (358, 3, NULL, 2, 2, 0, 20, '2025-05-12 17:37:19', '2025-05-13 13:27:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (359, 4, NULL, 2, 2, 0, 10, '2025-05-12 17:41:39', '2025-05-14 12:43:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (360, 4, NULL, 2, 2, 0, 10, '2025-05-12 17:43:10', '2025-05-14 13:32:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (361, 4, NULL, 2, 2, 0, 12, '2025-05-12 17:44:21', '2025-05-14 13:53:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (362, 1, NULL, 2, 2, 0, 28, '2025-05-13 08:27:23', '2025-05-13 08:46:26', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (363, 3, NULL, 2, 2, 0, 25, '2025-05-13 14:47:50', '2025-05-13 15:21:50', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (364, 3, NULL, 2, 2, 0, 12, '2025-05-13 14:49:04', '2025-05-13 15:43:31', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (365, NULL, NULL, 1, 3, 0, 12, '2025-05-13 15:07:28', '2025-05-13 17:58:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (367, 2, NULL, 2, 2, 0, 12, '2025-05-13 16:23:27', '2025-05-14 07:56:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (368, 3, NULL, 1, 2, 0, 27, '2025-05-14 09:43:40', '2025-09-09 17:01:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (369, 2, NULL, 2, 2, 0, 12, '2025-05-14 12:41:58', '2025-05-15 07:52:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (370, 1, NULL, 2, 2, 0, 28, '2025-05-14 15:49:25', '2025-05-15 07:32:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (371, 3, NULL, 2, 2, 0, 27, '2025-05-15 08:40:34', '2025-09-09 17:01:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (372, 18, NULL, 2, 2, 0, 18, '2025-05-15 11:27:22', '2025-05-15 13:24:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (373, NULL, 2, 1, 3, 0, 12, '2025-05-15 12:33:37', '2025-05-17 13:04:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (374, 2, NULL, 2, 2, 0, 12, '2025-05-15 12:59:21', '2025-05-16 08:04:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (375, 18, NULL, 2, 2, 0, 18, '2025-05-15 17:21:53', '2025-05-19 10:46:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (376, 4, NULL, 2, 2, 0, 10, '2025-05-15 18:05:04', '2025-05-16 12:34:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (377, 4, NULL, 2, 2, 0, 10, '2025-05-15 18:05:46', '2025-05-16 13:55:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (378, 4, NULL, 2, 2, 0, 10, '2025-05-15 18:07:26', '2025-05-16 13:09:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (379, NULL, NULL, 1, 3, 0, 12, '2025-05-15 18:16:03', '2025-05-19 12:23:22', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (380, 3, NULL, 2, 2, 0, 27, '2025-05-16 09:06:47', '2025-09-09 17:00:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (381, 1, NULL, 2, 2, 0, 28, '2025-05-16 09:13:03', '2025-05-17 08:09:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (382, NULL, 2, 1, 3, 0, 12, '2025-05-16 10:51:19', '2025-05-17 08:36:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (384, 4, NULL, 2, 2, 0, 12, '2025-05-16 12:14:09', '2025-05-16 14:32:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (385, 4, NULL, 2, 2, 0, 12, '2025-05-17 10:48:09', '2025-05-19 13:27:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (386, 3, NULL, 2, 2, 0, 27, '2025-05-17 10:56:37', '2025-09-09 17:02:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (387, 3, NULL, 2, 2, 0, 27, '2025-05-17 11:02:51', '2025-09-09 17:01:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (388, 3, NULL, 2, 2, 0, 27, '2025-05-17 11:05:55', '2025-09-09 17:02:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (390, 3, NULL, 2, 2, 0, 25, '2025-05-17 11:16:58', '2025-05-19 10:40:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (391, 3, NULL, 2, 2, 0, 12, '2025-05-17 11:21:38', '2025-05-19 11:41:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (392, 3, NULL, 2, 2, 0, 25, '2025-05-17 11:22:18', '2025-05-19 12:06:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (393, 3, NULL, 2, 2, 0, 20, '2025-05-17 11:23:07', '2025-05-19 12:48:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (394, 2, NULL, 2, 2, 0, 12, '2025-05-17 12:15:12', '2025-05-19 07:28:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (395, 18, NULL, 2, 2, 0, 18, '2025-05-17 12:45:53', '2025-05-19 13:51:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (396, 3, NULL, 2, 2, 0, 27, '2025-05-19 09:28:36', '2025-09-09 17:00:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (397, 2, NULL, 2, 2, 0, 12, '2025-05-19 12:47:58', '2025-05-19 16:48:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (398, 18, NULL, 2, 2, 0, 18, '2025-05-19 17:11:07', '2025-05-20 14:16:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (399, 3, NULL, 2, 2, 0, 25, '2025-05-20 08:32:39', '2025-05-20 11:41:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (400, 3, NULL, 2, 2, 0, 12, '2025-05-20 08:35:30', '2025-05-20 12:25:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (401, 3, NULL, 2, 2, 0, 25, '2025-05-20 08:37:48', '2025-05-20 12:58:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (402, 3, NULL, 2, 2, 0, 20, '2025-05-20 08:38:35', '2025-05-20 13:58:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (403, 2, NULL, 2, 2, 0, 12, '2025-05-20 08:40:46', '2025-05-20 16:21:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (404, 3, NULL, 2, 2, 0, 12, '2025-05-20 15:01:45', '2025-05-21 13:02:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (405, 3, NULL, 2, 2, 0, 25, '2025-05-21 08:50:16', '2025-05-21 13:21:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (406, 3, NULL, 2, 2, 0, 20, '2025-05-21 08:51:28', '2025-05-21 13:46:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (407, 18, NULL, 2, 2, 0, 18, '2025-05-21 08:52:26', '2025-05-21 14:13:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (408, 1, NULL, 2, 2, 0, 28, '2025-05-21 08:53:32', '2025-05-22 08:02:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (409, 2, NULL, 2, 2, 0, 31, '2025-05-21 09:11:39', '2025-05-22 15:50:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (410, 18, NULL, 2, 2, 0, 18, '2025-05-21 09:13:25', '2025-05-22 14:46:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (411, 2, NULL, 2, 2, 0, 12, '2025-05-21 09:15:54', '2025-05-22 08:18:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (412, 4, NULL, 2, 2, 0, 10, '2025-05-21 16:58:35', '2025-05-22 08:48:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (413, 4, NULL, 2, 2, 0, 10, '2025-05-21 16:59:36', '2025-05-22 09:07:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (414, 4, NULL, 2, 2, 0, 12, '2025-05-21 17:01:31', '2025-05-22 09:40:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (415, 3, NULL, 2, 2, 0, 25, '2025-05-22 08:40:53', '2025-05-22 13:25:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (416, 3, NULL, 2, 2, 0, 25, '2025-05-22 08:41:08', '2025-05-22 12:05:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (417, 3, NULL, 2, 2, 0, 12, '2025-05-22 08:42:23', '2025-05-22 13:00:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (418, 3, NULL, 2, 2, 0, 12, '2025-05-22 08:44:09', '2025-05-22 14:01:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (419, 3, NULL, 2, 2, 0, 20, '2025-05-22 08:45:24', '2025-05-22 14:25:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (420, 3, NULL, NULL, NULL, 0, 27, '2025-05-22 09:45:56', '2025-09-09 16:59:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (421, 1, NULL, 2, 2, 0, 28, '2025-05-23 08:40:16', '2025-05-23 10:16:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (422, 3, NULL, NULL, NULL, 0, 27, '2025-05-23 08:44:40', '2025-09-09 16:58:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (423, 18, NULL, 2, 2, 0, 18, '2025-05-23 08:46:30', '2025-05-23 15:21:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (424, 3, NULL, 2, 2, 0, 20, '2025-05-23 08:48:42', '2025-05-23 12:27:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (425, 2, NULL, 2, 2, 0, 12, '2025-05-23 11:11:00', '2025-05-23 11:37:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (426, 2, NULL, 2, 2, 0, 12, '2025-05-23 13:45:12', '2025-05-23 14:07:52', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (427, 2, NULL, 2, 2, 0, 12, '2025-05-23 16:55:20', '2025-05-24 07:55:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (429, 4, NULL, 2, 2, 0, 10, '2025-05-24 09:34:17', '2025-05-26 12:37:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (430, 4, NULL, 2, 2, 0, 10, '2025-05-24 09:35:56', '2025-05-26 13:20:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (431, 4, NULL, 2, 2, 0, 12, '2025-05-24 09:36:50', '2025-05-26 13:42:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (432, 3, NULL, 2, 2, 0, 25, '2025-05-26 09:40:14', '2025-05-26 10:44:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (433, 3, NULL, 2, 2, 0, 12, '2025-05-26 09:44:56', '2025-05-26 11:14:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (434, 3, NULL, 2, 2, 0, 12, '2025-05-26 09:46:02', '2025-05-26 12:06:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (435, 3, NULL, 2, 2, 0, 25, '2025-05-26 09:46:45', '2025-05-26 12:56:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (436, 2, NULL, 2, 2, 0, 12, '2025-05-26 17:28:20', '2025-05-27 08:56:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (437, 1, NULL, 2, 2, 0, 28, '2025-05-27 08:27:58', '2025-05-27 10:21:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (438, 3, NULL, 2, 2, 0, 25, '2025-05-27 09:07:53', '2025-06-03 11:31:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (439, 3, NULL, 2, 2, 0, 12, '2025-05-27 09:08:47', '2025-06-03 11:31:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (442, 3, NULL, 2, 2, 0, 20, '2025-05-27 09:11:34', '2025-06-03 11:31:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (446, 3, NULL, 2, 2, 0, 25, '2025-05-28 08:43:30', '2025-05-28 12:04:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (447, 3, NULL, 2, 2, 0, 12, '2025-05-28 08:45:03', '2025-05-28 12:38:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (448, 3, NULL, 2, 2, 0, 25, '2025-05-28 08:47:08', '2025-05-28 13:05:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (449, 3, NULL, 2, 2, 0, 12, '2025-05-28 08:47:51', '2025-05-28 13:29:09', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (450, 3, NULL, 2, 2, 0, 20, '2025-05-28 08:49:23', '2025-05-28 13:54:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (451, 5, NULL, 1, 2, 0, 4, '2025-05-28 11:30:56', '2025-05-29 07:46:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (452, 5, NULL, 2, 2, 0, 4, '2025-05-28 16:17:45', '2025-05-30 09:26:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (453, 4, NULL, 2, 2, 0, 12, '2025-05-28 16:42:03', '2025-06-02 18:00:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (454, 4, NULL, 2, 2, 0, 12, '2025-05-28 16:43:14', '2025-05-29 14:04:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (455, 2, NULL, 2, 2, 0, 12, '2025-05-28 17:49:14', '2025-05-29 12:48:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (456, 3, NULL, 2, 2, 0, 10, '2025-05-29 06:33:19', '2025-05-30 12:34:50', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (457, 3, NULL, 2, 2, 2, 20, '2025-05-29 08:48:42', '2025-05-29 13:09:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (458, 22, NULL, 1, 3, 0, 12, '2025-05-29 09:32:44', '2025-05-31 09:42:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (459, 4, NULL, 2, 2, 0, 12, '2025-05-29 10:06:03', '2025-05-29 14:24:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (460, 1, NULL, 2, 2, 1, NULL, '2025-05-29 10:49:06', '2025-05-29 10:49:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (461, 1, NULL, 2, 2, 0, 28, '2025-05-29 10:51:48', '2025-05-29 13:49:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (462, 2, NULL, 2, 2, 0, 12, '2025-05-29 17:16:23', '2025-05-30 06:55:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (463, 3, NULL, 2, 2, 0, 25, '2025-05-30 09:22:54', '2025-05-30 11:42:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (464, 3, NULL, 2, 2, 0, 12, '2025-05-30 09:24:30', '2025-05-30 12:09:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (465, 3, NULL, 2, 2, 0, 25, '2025-05-30 09:27:16', '2025-05-30 12:27:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (466, 3, NULL, 2, 2, 0, 12, '2025-05-30 09:28:44', '2025-05-30 12:41:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (467, 3, NULL, 2, 2, 2, 20, '2025-05-30 09:29:38', '2025-05-30 13:34:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (468, 2, NULL, 2, 2, 0, 12, '2025-05-30 14:51:21', '2025-05-30 16:19:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (469, 2, NULL, 2, 2, 0, 12, '2025-05-30 14:59:21', '2025-05-31 06:59:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (470, 2, NULL, 2, 2, 0, 12, '2025-05-30 15:32:18', '2025-05-31 11:31:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (471, 18, NULL, 2, 2, 0, 10, '2025-06-02 08:47:17', '2025-06-02 13:33:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (472, 2, NULL, 2, 2, 0, 12, '2025-06-02 09:16:55', '2025-06-02 09:47:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (473, 18, NULL, 2, 2, 0, 10, '2025-06-02 14:24:50', '2025-06-03 14:04:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (474, 2, NULL, 2, 2, 0, 12, '2025-06-03 08:42:56', '2025-06-03 15:58:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (475, 18, NULL, 2, 2, 0, 18, '2025-06-03 09:00:46', '2025-06-03 13:40:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (476, 1, NULL, 2, 2, 0, 28, '2025-06-03 12:38:46', '2025-06-04 10:40:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (477, 2, NULL, 2, 2, 0, 12, '2025-06-03 17:14:26', '2025-06-04 08:13:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (478, 4, NULL, 2, 2, 0, 12, '2025-06-03 17:35:32', '2025-06-04 08:33:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (479, 3, NULL, 2, 2, 0, 25, '2025-06-04 08:28:43', '2025-06-04 12:28:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (480, 3, NULL, 2, 2, 0, 12, '2025-06-04 08:30:03', '2025-06-04 12:53:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (481, 3, NULL, 2, 2, 0, 25, '2025-06-04 08:32:18', '2025-06-04 13:51:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (482, 3, NULL, 2, 2, 0, 20, '2025-06-04 08:39:16', '2025-06-04 14:37:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (483, 18, NULL, 2, 2, 0, 10, '2025-06-04 10:54:47', '2025-06-04 14:10:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (484, 2, NULL, 2, 2, 0, 31, '2025-06-04 14:29:57', '2025-06-05 09:42:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (485, 18, NULL, 2, 2, 0, 10, '2025-06-04 14:39:25', '2025-06-05 14:49:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (486, 3, NULL, 2, 2, 0, 25, '2025-06-04 16:36:16', '2025-06-05 12:34:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (487, 3, NULL, 2, 2, 0, 12, '2025-06-04 16:43:07', '2025-06-05 13:44:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (488, 3, NULL, 2, 2, 0, 20, '2025-06-04 16:44:17', '2025-06-05 14:35:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (489, 2, NULL, 2, 2, 0, 12, '2025-06-04 16:45:16', '2025-06-05 12:57:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (490, 18, NULL, 2, 2, 0, 18, '2025-06-04 16:46:36', '2025-06-05 13:27:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (491, 18, NULL, 2, 2, 2, 18, '2025-06-05 10:25:09', '2025-06-06 14:02:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (492, 1, NULL, 2, 2, 0, 28, '2025-06-05 15:13:25', '2025-06-06 10:39:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (493, 3, NULL, 2, 2, 0, 12, '2025-06-06 09:32:25', '2025-06-06 11:08:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (494, 3, NULL, 2, 2, 0, 25, '2025-06-06 09:33:58', '2025-06-06 12:34:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (495, 3, NULL, 2, 2, 0, 12, '2025-06-06 09:35:00', '2025-06-06 12:57:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (496, 3, NULL, 2, 2, 0, 25, '2025-06-06 09:37:03', '2025-06-06 13:43:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (497, 3, NULL, 2, 2, 0, 20, '2025-06-06 09:38:29', '2025-06-06 14:18:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (498, 18, NULL, 2, 2, 0, 10, '2025-06-09 08:40:39', '2025-06-09 13:57:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (499, NULL, 8, 1, 3, 0, 12, '2025-06-10 08:41:19', '2025-06-10 10:11:02', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (501, NULL, 8, 1, 3, 0, 2, '2025-06-10 08:44:20', '2025-06-10 14:41:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (502, 18, NULL, 2, 2, 0, 10, '2025-06-10 08:48:22', '2025-06-10 13:28:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (503, 18, NULL, 2, 2, 0, 18, '2025-06-10 08:50:07', '2025-06-10 16:35:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (504, NULL, 1, 1, 3, 0, 12, '2025-06-10 10:42:31', '2025-06-10 11:28:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (505, 21, NULL, 1, 2, 0, 20, '2025-06-10 10:56:11', '2025-06-10 16:24:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (506, 18, NULL, 2, 2, 0, 10, '2025-06-11 09:52:14', '2025-06-11 13:31:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (507, 18, NULL, 2, 2, 0, 10, '2025-06-12 08:53:35', '2025-06-12 13:57:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (508, 4, NULL, 2, 2, 0, 12, '2025-06-12 16:13:05', '2025-06-13 09:43:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (509, 16, NULL, 1, 3, 0, 12, '2025-06-13 17:46:02', '2025-06-14 10:00:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (511, NULL, NULL, 1, 3, 0, 27, '2025-06-14 09:59:42', '2025-09-09 16:58:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (512, NULL, NULL, 1, 3, 0, 27, '2025-06-14 10:00:18', '2025-09-09 16:58:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (513, 4, NULL, 2, 2, 0, 10, '2025-06-14 13:24:17', '2025-06-17 12:35:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (514, NULL, NULL, 1, 3, 0, 27, '2025-06-16 08:41:47', '2025-09-09 16:57:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (515, 18, NULL, 2, 2, 0, 10, '2025-06-16 08:48:28', '2025-06-17 14:34:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (516, 2, NULL, 2, 2, 0, 12, '2025-06-16 14:07:13', '2025-06-16 14:48:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (517, 2, NULL, 2, 2, 0, 12, '2025-06-17 12:12:30', '2025-06-17 17:00:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (518, 2, NULL, 2, 2, 2, 12, '2025-06-17 16:51:42', '2025-06-18 09:53:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (520, NULL, 8, 1, 3, 0, 16, '2025-06-18 15:26:00', '2025-06-18 15:31:55', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (521, 18, NULL, 2, 2, 0, 10, '2025-06-18 16:36:14', '2025-06-19 14:30:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (522, 2, NULL, 2, 2, 0, 12, '2025-06-19 13:14:50', '2025-06-19 13:48:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (523, NULL, 1, 1, 3, 0, 12, '2025-06-19 15:35:24', '2025-06-20 09:29:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (524, 4, NULL, 2, 2, 2, 10, '2025-06-19 15:49:47', '2025-06-20 10:50:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (525, 18, NULL, 2, 2, 2, 36, '2025-06-23 08:38:27', '2025-06-23 13:15:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (526, NULL, 3, 1, 3, 0, 12, '2025-06-23 08:58:29', '2025-06-23 10:03:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (527, 18, NULL, 2, 2, 0, 10, '2025-06-23 09:49:38', '2025-06-23 14:06:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (528, 16, NULL, 1, 3, 0, 12, '2025-06-23 10:17:31', '2025-06-24 09:12:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (530, NULL, 8, 1, 3, 0, 20, '2025-06-25 08:39:01', '2025-06-25 10:50:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (531, 18, NULL, 2, 2, 0, 10, '2025-06-25 10:59:48', '2025-06-26 10:20:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (532, NULL, 8, 1, 3, 0, 20, '2025-06-25 12:59:13', '2025-06-26 09:57:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (534, 2, NULL, 2, 2, 0, 12, '2025-06-26 08:46:35', '2025-06-26 12:50:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (535, NULL, 1, 1, 3, 2, 12, '2025-06-26 10:04:57', '2025-06-27 09:39:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (536, 18, NULL, 2, 2, 0, 10, '2025-06-26 10:19:20', '2025-06-26 13:51:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (538, NULL, 3, 1, 3, 0, 12, '2025-06-27 11:31:52', '2025-06-28 09:27:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (539, NULL, NULL, 1, 3, 0, 12, '2025-06-27 17:36:35', '2025-06-28 13:16:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (540, NULL, 3, 1, 3, 0, 12, '2025-06-27 17:49:37', '2025-06-28 09:03:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (542, NULL, 1, 1, 3, 2, 12, '2025-06-28 08:27:40', '2025-06-30 09:04:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (543, NULL, 1, 1, 3, 0, 12, '2025-06-28 08:29:02', '2025-06-28 16:30:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (544, 2, NULL, 2, 2, 0, 12, '2025-06-28 08:41:19', '2025-06-28 14:53:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (545, NULL, NULL, NULL, NULL, 0, 27, '2025-06-28 11:02:14', '2025-09-09 16:52:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (546, 18, NULL, 2, 2, 0, 10, '2025-06-30 09:30:04', '2025-06-30 09:51:04', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (547, 18, NULL, 2, 2, 0, 10, '2025-07-01 09:22:46', '2025-07-01 16:05:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (548, 18, NULL, 2, 2, 0, 10, '2025-07-01 09:29:40', '2025-07-02 10:53:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (549, 21, NULL, 1, 2, 0, 20, '2025-07-01 11:02:22', '2025-07-01 16:03:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (550, 21, NULL, 1, 2, 0, 20, '2025-07-01 11:34:09', '2025-07-01 14:15:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (551, NULL, NULL, NULL, NULL, 0, 27, '2025-07-01 16:38:04', '2025-09-09 16:51:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (552, 21, NULL, 1, 2, 0, 20, '2025-07-02 09:28:57', '2025-07-02 11:25:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (553, 18, NULL, 2, 2, 0, 10, '2025-07-02 10:29:53', '2025-07-02 14:47:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (554, NULL, NULL, NULL, NULL, 0, 27, '2025-07-02 13:18:19', '2025-08-04 12:53:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (555, 21, NULL, 1, 2, 0, 20, '2025-07-03 10:22:19', '2025-07-03 11:08:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (556, 18, NULL, 2, 2, 0, 10, '2025-07-03 11:05:17', '2025-07-03 13:23:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (557, NULL, 1, 1, 3, 0, 12, '2025-07-03 17:41:56', '2025-07-05 11:45:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (558, NULL, 3, 1, 3, 0, 12, '2025-07-04 08:28:42', '2025-07-04 11:15:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (559, 18, NULL, 2, 2, 0, 10, '2025-07-04 10:52:35', '2025-07-04 13:24:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (560, NULL, 3, 1, 3, 0, 10, '2025-07-04 16:23:37', '2025-07-04 16:44:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (561, NULL, 10, 1, 3, 0, 12, '2025-07-04 16:28:11', '2025-07-05 06:09:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (562, NULL, 1, 1, 3, 0, 12, '2025-07-04 16:32:18', '2025-07-05 11:48:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (563, NULL, 1, 1, 3, 0, 12, '2025-07-04 16:33:52', '2025-07-05 11:48:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (564, NULL, 1, 1, 3, 0, 12, '2025-07-04 16:35:07', '2025-07-05 17:02:24', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (565, NULL, NULL, 1, 3, 2, 12, '2025-07-05 08:46:11', '2025-07-08 09:48:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (566, NULL, 1, 1, 3, 0, 12, '2025-07-05 11:41:14', '2025-07-05 15:02:24', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (567, NULL, 4, 1, 3, 0, 6, '2025-07-05 12:49:10', '2025-07-05 13:05:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (568, NULL, 1, 1, 3, 0, 12, '2025-07-07 10:37:01', '2025-07-07 11:37:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (569, NULL, NULL, NULL, NULL, 0, 27, '2025-07-07 15:06:30', '2025-08-04 13:07:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (570, 18, NULL, 2, 2, 0, 36, '2025-07-07 17:25:36', '2025-07-08 17:24:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (571, NULL, NULL, 1, 3, 0, 20, '2025-07-08 12:06:37', '2025-07-08 14:14:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (572, 1, NULL, 2, 2, 0, 28, '2025-07-08 16:41:37', '2025-07-09 15:30:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (573, NULL, NULL, 1, 3, 0, 12, '2025-07-08 16:43:40', '2025-07-09 08:43:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (574, 18, NULL, 2, 2, 0, 36, '2025-07-09 12:48:00', '2025-07-09 13:15:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (575, 18, NULL, 2, 2, 0, 10, '2025-07-09 12:49:06', '2025-07-09 13:50:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (576, 18, NULL, 2, 2, 0, 36, '2025-07-10 16:07:13', '2025-07-11 08:45:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (578, 1, NULL, 2, 2, 0, 28, '2025-07-11 12:46:37', '2025-07-12 09:03:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (579, NULL, NULL, 1, 3, 0, 12, '2025-07-11 12:48:50', '2025-07-12 09:50:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (581, NULL, 7, NULL, NULL, 0, 27, '2025-07-11 17:42:58', '2025-07-12 13:54:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (582, NULL, 1, 1, 3, 0, 12, '2025-07-12 08:49:47', '2025-07-14 08:38:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (583, NULL, 1, 1, 3, 0, 12, '2025-07-12 08:51:35', '2025-07-14 08:41:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (584, NULL, 1, 1, 3, 0, 12, '2025-07-12 08:56:08', '2025-07-12 17:56:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (585, NULL, NULL, NULL, NULL, 0, 27, '2025-07-12 10:13:07', '2025-08-04 13:09:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (586, NULL, 7, 1, 3, 0, 27, '2025-07-12 12:27:16', '2025-07-12 13:53:50', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (587, NULL, 9, 1, 3, 2, 24, '2025-07-12 15:23:44', '2025-08-04 13:10:26', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (588, 18, NULL, 2, 2, 0, 10, '2025-07-14 11:28:58', '2025-07-14 12:43:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (589, 18, NULL, 2, 2, 0, 10, '2025-07-14 11:30:22', '2025-07-16 12:33:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (590, NULL, 9, 1, 3, 0, 24, '2025-07-14 16:33:30', '2025-07-21 10:37:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (591, NULL, 1, 1, 3, 0, 12, '2025-07-15 08:10:49', '2025-07-15 09:50:27', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (592, NULL, NULL, NULL, NULL, 0, 27, '2025-07-15 14:00:53', '2025-08-04 13:11:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (593, 18, NULL, 2, 2, 0, 10, '2025-07-15 17:57:42', '2025-07-16 12:33:29', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (594, 1, NULL, 2, 2, 0, 28, '2025-07-16 09:49:54', '2025-07-16 13:32:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (596, 18, NULL, 2, 2, 0, 10, '2025-07-16 17:07:43', '2025-07-17 11:39:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (597, 18, NULL, 2, 2, 0, 10, '2025-07-16 17:08:34', '2025-07-17 14:10:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (598, 21, NULL, 2, 2, 0, 20, '2025-07-17 11:10:18', '2025-07-17 11:56:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (599, 18, NULL, 2, 2, 0, 10, '2025-07-17 17:47:16', '2025-07-18 14:10:19', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (600, 21, NULL, 1, 2, 0, 20, '2025-07-18 11:03:00', '2025-07-18 11:55:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (601, 1, NULL, 2, 2, 0, 28, '2025-07-18 11:49:22', '2025-07-18 15:51:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (603, NULL, NULL, 1, 2, 0, 12, '2025-07-18 15:04:31', '2025-07-19 13:01:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (604, NULL, 1, 1, 3, 0, 12, '2025-07-18 15:05:54', '2025-07-19 10:33:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (605, 2, NULL, 2, 2, 0, 12, '2025-07-18 17:23:44', '2025-07-19 09:17:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (607, 18, NULL, 2, 2, 2, 10, '2025-07-19 13:55:41', '2025-09-09 16:06:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (608, 21, NULL, 1, 2, 0, 20, '2025-07-21 09:48:02', '2025-07-21 13:11:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (609, NULL, NULL, 1, 3, 0, 12, '2025-07-22 10:29:37', '2025-07-22 13:59:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (610, NULL, 8, 1, 3, 0, 27, '2025-07-22 11:24:10', '2025-08-04 13:16:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (611, 24, NULL, 2, 2, 0, 10, '2025-07-22 16:59:27', '2025-07-26 13:55:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (612, 18, NULL, 2, 2, 2, 10, '2025-07-24 08:14:41', '2025-08-04 13:23:33', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (613, 18, NULL, 2, 2, 0, 10, '2025-07-24 09:17:59', '2025-07-24 10:48:42', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (614, NULL, 9, 1, 3, 0, 25, '2025-07-24 14:01:19', '2025-07-25 13:44:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (615, 2, NULL, 2, 2, 0, 12, '2025-07-24 15:20:04', '2025-07-24 15:42:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (616, 18, NULL, 2, 2, 0, 10, '2025-07-24 17:41:58', '2025-07-25 09:10:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (617, 18, NULL, 2, 2, 0, 10, '2025-07-24 18:04:46', '2025-07-25 14:20:10', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (618, 2, NULL, 2, 2, 0, 12, '2025-07-25 08:51:57', '2025-07-25 15:44:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (619, NULL, 2, 1, 3, 0, 12, '2025-07-25 09:58:53', '2025-07-26 15:20:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (620, NULL, NULL, 1, 3, 0, 12, '2025-07-25 11:44:20', '2025-07-26 10:29:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (621, NULL, NULL, 1, 1, 0, 6, '2025-07-25 12:31:24', '2025-07-26 09:41:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (622, NULL, 1, 1, 3, 2, 12, '2025-07-26 12:09:47', '2025-08-04 13:20:20', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (623, 18, NULL, 2, 2, 0, 10, '2025-07-30 08:44:52', '2025-07-30 10:19:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (624, 18, NULL, 2, 2, 0, 10, '2025-07-30 08:45:44', '2025-07-30 11:55:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (625, 18, NULL, 2, 2, 0, 10, '2025-07-30 14:33:58', '2025-07-31 16:53:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (626, 21, NULL, 1, 2, 0, 20, '2025-07-30 14:35:16', '2025-07-31 13:37:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (627, 18, NULL, NULL, 2, 0, 10, '2025-07-31 08:45:28', '2025-08-01 10:14:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (628, NULL, 8, 1, 3, 0, 12, '2025-07-31 16:58:09', '2025-08-01 12:02:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (629, 2, NULL, 2, 2, 0, 12, '2025-08-01 09:58:02', '2025-08-01 15:13:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (630, 21, NULL, 2, 2, 0, 20, '2025-08-01 12:30:12', '2025-08-01 14:43:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (631, NULL, NULL, 1, 3, 0, 27, '2025-08-01 14:53:51', '2025-08-04 13:21:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (632, NULL, NULL, 1, 1, 0, 12, '2025-08-01 14:56:29', '2025-08-05 09:32:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (633, NULL, NULL, NULL, NULL, 0, 27, '2025-08-01 16:13:12', '2025-08-04 13:22:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (634, NULL, 1, 1, 3, 0, 12, '2025-08-01 17:25:01', '2025-08-02 13:15:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (635, 18, NULL, 2, 2, 0, 10, '2025-08-04 08:41:51', '2025-08-11 15:07:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (636, 18, NULL, 2, 2, 0, 10, '2025-08-04 08:44:23', '2025-08-04 14:09:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (637, 18, NULL, 2, 2, 0, 10, '2025-08-05 08:35:52', '2025-08-05 10:50:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (638, 18, NULL, 2, 2, 0, 10, '2025-08-05 08:36:44', '2025-08-05 13:56:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (639, 2, NULL, 2, 2, 0, 12, '2025-08-05 16:15:33', '2025-08-05 16:50:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (640, 18, NULL, 2, 2, 2, 10, '2025-08-07 08:47:31', '2025-09-09 16:07:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (641, 18, NULL, 2, 2, 0, 10, '2025-08-07 08:48:26', '2025-08-07 13:48:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (642, 2, NULL, 2, 2, 0, 12, '2025-08-07 09:05:07', '2025-08-07 16:42:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (643, 21, NULL, NULL, NULL, 0, 12, '2025-08-07 09:42:13', '2025-08-07 14:22:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (644, 21, NULL, 1, 2, 0, 20, '2025-08-08 09:24:33', '2025-08-08 14:35:11', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (645, 25, 1, 1, 3, 0, 12, '2025-08-08 11:11:04', '2025-08-08 12:12:31', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (646, 18, NULL, 2, 2, 0, 10, '2025-08-08 13:16:42', '2025-08-08 13:52:52', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (647, 2, NULL, 1, 2, 0, 12, '2025-08-08 16:30:18', '2025-08-08 17:00:52', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (648, 25, 1, 1, 3, 0, 12, '2025-08-08 17:54:20', '2025-08-09 08:09:53', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (649, NULL, NULL, 1, 3, 1, NULL, '2025-08-08 17:57:01', '2025-08-08 17:57:01', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (650, NULL, 7, 1, 3, 0, 27, '2025-08-08 18:00:28', '2025-09-09 16:39:31', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (651, 18, NULL, 2, 2, 0, 10, '2025-08-11 08:14:27', '2025-08-11 09:45:51', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (652, 28, 6, 1, 3, 0, 10, '2025-08-11 09:24:41', '2025-08-11 12:08:20', 'S', 1);
INSERT INTO `administracion_pedidos` VALUES (653, NULL, 6, 1, 3, 0, 20, '2025-08-11 10:37:16', '2025-08-11 13:38:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (654, NULL, 6, 1, 3, 0, 12, '2025-08-11 12:07:46', '2025-08-11 12:55:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (655, NULL, 6, 1, 3, 0, 12, '2025-08-11 14:43:52', '2025-08-11 15:02:14', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (657, NULL, NULL, 1, 3, 2, 12, '2025-08-11 16:12:55', '2025-09-09 16:08:26', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (658, 18, NULL, 2, 2, 0, 10, '2025-08-12 08:55:10', '2025-08-12 10:09:47', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (659, 18, NULL, 2, 2, 0, 10, '2025-08-12 08:56:27', '2025-08-12 13:52:22', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (660, 21, NULL, 1, 2, 0, 20, '2025-08-12 10:34:50', '2025-08-12 11:40:18', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (661, 5, NULL, 2, 2, 0, 12, '2025-08-12 14:26:43', '2025-08-13 07:24:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (662, 5, NULL, 2, 2, 0, 12, '2025-08-13 10:57:59', '2025-08-13 11:35:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (663, 18, NULL, 2, 2, 0, 10, '2025-08-13 12:09:48', '2025-08-13 13:55:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (664, NULL, NULL, NULL, NULL, 0, 27, '2025-08-13 14:31:09', '2025-09-09 16:38:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (665, 18, NULL, 2, 2, 0, 10, '2025-08-14 08:40:45', '2025-08-14 10:17:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (666, 29, NULL, 1, 3, 0, 12, '2025-08-14 12:31:33', '2025-08-14 14:38:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (667, 18, NULL, 2, 2, 0, 10, '2025-08-14 16:42:11', '2025-08-15 13:56:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (668, 30, NULL, 1, 2, 0, 28, '2025-08-15 12:37:03', '2025-08-16 08:58:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (669, 18, NULL, 2, 2, 0, 10, '2025-08-18 08:52:22', '2025-08-18 11:33:38', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (670, NULL, NULL, 2, 2, 0, 12, '2025-08-19 11:10:24', '2025-08-19 15:32:23', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (671, 18, NULL, 2, 2, 0, 10, '2025-08-19 12:05:37', '2025-08-19 13:47:37', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (672, NULL, NULL, 1, 3, 2, 12, '2025-08-19 15:26:37', '2025-09-09 16:14:46', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (673, 26, NULL, 1, 3, 2, 12, '2025-08-19 18:29:36', '2025-09-09 16:18:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (674, 18, NULL, 2, 2, 0, 10, '2025-08-20 08:54:07', '2025-08-20 10:16:08', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (675, 18, NULL, NULL, NULL, 1, 10, '2025-08-20 08:55:01', '2025-08-21 09:15:54', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (676, 18, NULL, 2, 2, 0, 10, '2025-08-21 11:08:05', '2025-08-21 13:44:14', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (677, 18, NULL, 2, 2, 0, 10, '2025-08-21 11:09:03', '2025-08-21 14:08:45', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (678, 5, NULL, 1, 3, 0, 12, '2025-08-21 16:52:02', '2025-08-22 15:35:58', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (679, 18, NULL, 2, 2, 0, 36, '2025-08-22 09:02:28', '2025-08-22 10:04:10', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (680, 2, NULL, 2, 2, 0, 12, '2025-08-22 10:53:27', '2025-08-22 11:12:48', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (681, 26, NULL, 1, 3, 2, 12, '2025-08-22 13:24:51', '2025-09-09 16:16:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (682, 25, 1, 1, 3, 0, 12, '2025-08-22 18:14:51', '2025-08-23 09:26:31', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (683, 25, 1, 1, 3, 0, 16, '2025-08-23 09:41:18', '2025-08-23 11:08:35', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (684, 11, NULL, 1, 2, 0, 12, '2025-08-23 10:43:06', '2025-08-25 09:21:24', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (685, NULL, NULL, NULL, NULL, 0, 12, '2025-08-23 12:47:57', '2025-08-25 11:57:55', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (686, 18, NULL, 2, 2, 0, 18, '2025-08-23 13:51:05', '2025-08-25 09:05:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (687, 18, NULL, 2, 2, 0, 36, '2025-08-25 08:32:59', '2025-08-25 09:46:39', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (688, 18, NULL, 2, 2, 0, 10, '2025-08-25 08:33:59', '2025-08-25 10:04:34', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (689, 18, NULL, 2, 2, 0, 10, '2025-08-25 08:35:41', '2025-08-25 10:25:23', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (690, NULL, NULL, 1, 3, 0, 12, '2025-08-25 11:59:00', '2025-08-25 12:00:12', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (691, 31, 10, 1, 2, 0, 12, '2025-08-25 12:43:28', '2025-08-25 13:39:19', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (692, 18, NULL, 2, 2, 0, 10, '2025-08-26 09:19:14', '2025-08-26 13:43:01', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (693, 2, NULL, 2, 2, 0, 12, '2025-08-26 14:32:10', '2025-08-26 16:09:34', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (694, 18, NULL, 2, 2, 0, 10, '2025-08-27 08:36:14', '2025-08-27 09:56:36', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (695, 21, NULL, 1, 2, 0, 16, '2025-08-27 11:40:52', '2025-08-27 13:31:58', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (696, NULL, 3, 1, 3, 0, 12, '2025-08-27 17:13:02', '2025-08-27 17:41:24', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (697, 18, NULL, 2, 2, 0, 10, '2025-08-28 08:53:06', '2025-08-28 10:07:47', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (698, 18, NULL, 2, 2, 0, 10, '2025-08-28 08:53:52', '2025-08-28 14:06:16', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (699, 5, NULL, 1, 3, 0, 12, '2025-08-28 14:26:49', '2025-09-02 11:10:04', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (700, 25, 1, 1, 3, 0, 12, '2025-08-28 17:57:18', '2025-08-29 09:27:27', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (701, 18, NULL, 2, 2, 2, 10, '2025-08-29 09:27:05', '2025-09-09 16:12:43', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (702, 18, NULL, 2, 2, 0, 10, '2025-08-29 09:28:22', '2025-08-29 13:45:36', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (703, 2, NULL, 2, 2, 0, 12, '2025-08-29 12:42:08', '2025-08-29 15:04:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (704, NULL, 9, 1, 3, 0, 12, '2025-09-01 09:16:13', '2025-09-01 09:42:09', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (705, 21, NULL, 1, 2, 0, 16, '2025-09-01 12:22:37', '2025-09-01 13:40:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (706, NULL, NULL, NULL, NULL, 0, 27, '2025-09-01 12:38:46', '2025-09-09 16:32:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (707, 2, NULL, 2, 2, 0, 12, '2025-09-01 14:48:51', '2025-09-01 15:57:46', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (708, 18, NULL, 2, 2, 0, 10, '2025-09-02 08:26:18', '2025-09-02 08:53:46', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (709, 18, NULL, 2, 2, 0, 10, '2025-09-02 08:27:51', '2025-09-02 10:24:54', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (710, NULL, NULL, NULL, NULL, 0, 27, '2025-09-02 14:29:37', '2025-09-09 16:31:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (711, 2, NULL, 2, 2, 0, 12, '2025-09-02 15:14:24', '2025-09-02 16:47:20', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (712, 18, NULL, 2, 2, 0, 10, '2025-09-03 10:38:11', '2025-09-03 14:24:23', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (713, 21, NULL, 1, 2, 0, 16, '2025-09-03 11:19:12', '2025-09-03 14:09:13', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (714, 2, NULL, 2, 2, 0, 12, '2025-09-03 16:15:44', '2025-09-03 16:40:56', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (715, 18, NULL, 2, 2, 0, 10, '2025-09-04 08:58:29', '2025-09-04 09:46:53', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (716, 28, 9, 1, 3, 2, 25, '2025-09-04 09:21:48', '2025-09-09 16:10:19', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (717, 2, NULL, 2, 2, 0, 12, '2025-09-04 14:44:26', '2025-09-04 15:27:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (718, NULL, 9, 1, 3, 0, 25, '2025-09-04 15:21:45', '2025-09-04 15:49:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (719, 18, NULL, 2, 2, 0, 10, '2025-09-04 17:40:50', '2025-09-05 10:15:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (720, 18, NULL, 2, 2, 0, 10, '2025-09-05 13:10:44', '2025-09-05 13:43:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (721, 2, NULL, NULL, NULL, 0, 12, '2025-09-05 14:06:53', '2025-09-05 16:30:19', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (722, 32, NULL, 1, 2, 0, 12, '2025-09-05 16:43:43', '2025-09-06 07:33:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (723, 25, 1, 1, 3, 2, 27, '2025-09-05 17:55:27', '2025-09-17 10:25:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (724, 25, 1, 1, 3, 0, 12, '2025-09-06 09:29:07', '2025-09-06 12:50:55', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (725, 2, NULL, 2, 2, 0, 12, '2025-09-06 11:30:37', '2025-09-06 14:54:31', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (726, 25, 1, 1, 3, 0, 12, '2025-09-06 14:32:22', '2025-09-08 12:54:26', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (727, 31, 10, 1, 2, 0, 12, '2025-09-06 14:34:22', '2025-09-08 14:15:31', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (728, 18, NULL, 2, 2, 0, 10, '2025-09-06 14:49:29', '2025-09-08 10:03:14', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (729, 18, NULL, 2, 2, 0, 10, '2025-09-06 14:50:23', '2025-09-08 10:22:48', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (730, 33, NULL, 1, 2, 0, 31, '2025-09-09 12:51:36', '2025-09-09 13:46:25', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (731, NULL, NULL, 1, 3, 0, 12, '2025-09-09 13:29:38', '2025-09-09 15:45:30', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (732, 18, NULL, 2, 2, 0, 10, '2025-09-09 17:48:15', '2025-09-10 10:00:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (733, 18, NULL, 2, 2, 0, 10, '2025-09-09 17:49:01', '2025-09-10 10:27:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (734, 2, NULL, 2, 2, 0, 37, '2025-09-10 16:48:14', '2025-09-10 17:34:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (735, 18, NULL, 2, 2, 0, 10, '2025-09-11 08:37:25', '2025-09-11 09:49:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (736, 18, NULL, 2, 2, 0, 10, '2025-09-11 08:38:10', '2025-09-11 13:47:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (737, 2, NULL, 2, 2, 0, 37, '2025-09-11 14:00:32', '2025-09-11 17:36:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (738, NULL, NULL, 1, 3, 0, 12, '2025-09-11 14:03:03', '2025-09-11 14:41:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (739, 2, NULL, 2, 2, 0, 12, '2025-09-11 14:55:30', '2025-09-11 16:31:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (740, 33, NULL, 2, 2, 0, 20, '2025-09-11 17:51:54', '2025-09-12 13:48:23', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (741, 18, NULL, 2, 2, 0, 10, '2025-09-11 17:54:11', '2025-09-12 10:02:19', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (742, 18, NULL, 2, 2, 0, 10, '2025-09-11 17:55:19', '2025-09-12 14:47:59', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (743, 2, NULL, 2, 2, 0, 12, '2025-09-11 17:57:15', '2025-09-12 16:49:11', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (744, 2, NULL, 2, 2, 0, 12, '2025-09-11 17:57:54', '2025-09-12 16:32:50', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (745, 2, NULL, NULL, NULL, 1, 37, '2025-09-11 17:58:54', '2025-09-12 16:02:10', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (746, 2, NULL, 2, 2, 0, 12, '2025-09-11 18:04:21', '2025-09-11 18:25:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (747, 25, 1, 1, 3, 2, 12, '2025-09-12 08:25:51', '2025-09-16 12:34:57', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (748, 21, NULL, 1, 2, 0, 16, '2025-09-12 17:26:30', '2025-09-13 09:17:59', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (749, NULL, NULL, 1, 3, 0, 12, '2025-09-12 17:27:34', '2025-09-13 08:46:53', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (750, 5, NULL, 1, 3, 0, 12, '2025-09-12 17:30:21', '2025-09-13 07:30:16', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (751, 31, 10, 1, 2, 0, 12, '2025-09-12 18:34:27', '2025-09-13 11:30:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (752, 2, NULL, 2, 2, 0, 12, '2025-09-13 10:28:56', '2025-09-13 15:58:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (753, 2, NULL, 2, 2, 0, 12, '2025-09-13 10:29:50', '2025-09-13 16:41:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (754, 2, NULL, 2, 2, 0, 37, '2025-09-13 13:08:38', '2025-09-13 16:55:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (755, 18, NULL, 2, 2, 0, 10, '2025-09-15 08:38:28', '2025-09-15 10:22:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (756, 2, NULL, 2, 2, 0, 12, '2025-09-15 08:39:12', '2025-09-15 16:55:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (757, 21, NULL, 1, 2, 0, 16, '2025-09-15 10:16:02', '2025-09-15 13:29:39', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (758, 2, NULL, 2, 2, 0, 12, '2025-09-15 15:08:51', '2025-09-15 16:38:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (759, 18, NULL, 2, 2, 0, 10, '2025-09-16 08:53:34', '2025-09-16 10:12:44', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (760, 21, NULL, 1, 2, 0, 12, '2025-09-16 11:29:16', '2025-09-16 11:59:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (761, 18, NULL, 2, 2, 0, 10, '2025-09-16 13:07:24', '2025-09-16 13:35:58', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (762, 31, NULL, 1, 2, 0, 12, '2025-09-16 15:48:30', '2025-09-16 17:06:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (763, 2, NULL, 2, 2, 0, 12, '2025-09-16 16:12:42', '2025-09-16 16:40:28', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (764, 2, NULL, 2, 2, 0, 12, '2025-09-16 16:14:11', '2025-09-16 17:31:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (765, NULL, 9, 1, 3, 0, 25, '2025-09-17 08:13:22', '2025-09-17 09:20:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (766, 25, 1, 2, 3, 0, 12, '2025-09-17 09:14:44', '2025-09-17 13:11:22', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (767, 21, NULL, 1, 2, 0, 12, '2025-09-17 10:00:39', '2025-09-17 12:18:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (768, 18, NULL, 2, 2, 0, 10, '2025-09-17 10:06:01', '2025-09-17 13:29:53', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (769, 2, NULL, 2, 2, 0, 12, '2025-09-17 16:09:49', '2025-09-19 10:42:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (770, 2, NULL, NULL, 2, 0, 12, '2025-09-17 16:10:42', '2025-09-17 17:04:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (771, 28, NULL, 1, 3, 0, 20, '2025-09-18 08:11:01', '2025-09-18 08:33:01', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (772, 18, NULL, 2, 2, 0, 10, '2025-09-18 08:22:15', '2025-09-18 09:48:45', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (773, 18, NULL, 2, 2, 0, 10, '2025-09-18 08:23:31', '2025-09-18 10:11:13', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (774, 21, NULL, 1, 2, 0, 12, '2025-09-18 10:26:20', '2025-09-18 12:22:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (775, NULL, 3, 1, 3, 0, 12, '2025-09-18 10:28:26', '2025-09-18 16:25:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (776, 28, NULL, 1, 3, 0, 20, '2025-09-18 13:05:09', '2025-09-18 13:28:31', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (777, 31, 10, 1, 2, 0, 12, '2025-09-18 13:45:11', '2025-09-18 14:53:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (778, 2, NULL, 2, 2, 0, 12, '2025-09-18 15:41:36', '2025-09-18 16:49:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (779, 21, NULL, 1, 2, 0, 12, '2025-09-19 08:53:07', '2025-09-19 10:46:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (780, 2, NULL, 2, 2, 0, 12, '2025-09-19 15:52:24', '2025-09-19 17:31:41', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (781, 31, NULL, 1, 2, 0, 12, '2025-09-20 09:12:59', '2025-09-20 10:35:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (782, NULL, NULL, 1, 3, 0, 6, '2025-09-20 11:25:35', '2025-09-20 11:58:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (783, NULL, 1, 1, 3, 0, 12, '2025-09-20 12:52:05', '2025-09-20 13:24:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (784, 2, NULL, 2, 2, 0, 12, '2025-09-20 13:28:33', '2025-09-20 15:25:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (785, 2, NULL, 2, 2, 0, 12, '2025-09-20 13:29:41', '2025-09-20 15:37:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (786, NULL, 1, 1, 2, 0, 12, '2025-09-20 15:03:27', '2025-09-21 08:18:46', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (787, 18, NULL, 2, 2, 0, 10, '2025-09-22 09:12:39', '2025-09-22 10:17:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (788, 18, NULL, 2, 2, 0, 10, '2025-09-22 09:13:28', '2025-09-22 11:08:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (789, 25, 1, 1, 3, 0, 12, '2025-09-22 09:44:44', '2025-09-22 10:39:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (790, 31, 10, 1, 2, 0, 12, '2025-09-22 15:33:35', '2025-09-22 16:03:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (791, 2, NULL, 2, 2, 0, 2, '2025-09-22 16:23:14', '2025-09-22 16:52:41', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (792, 18, NULL, 2, 2, 0, 10, '2025-09-23 09:29:37', '2025-09-23 10:28:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (793, 2, NULL, 2, 2, 0, 12, '2025-09-23 15:43:54', '2025-09-23 17:03:09', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (794, 2, NULL, 2, 2, 0, 12, '2025-09-23 15:44:57', '2025-09-23 17:44:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (795, 2, NULL, 2, 2, 0, 31, '2025-09-23 16:58:39', '2025-09-23 17:23:53', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (796, 18, NULL, 2, 2, 0, 10, '2025-09-25 09:29:53', '2025-09-25 13:34:46', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (797, 2, NULL, 2, 2, 0, 12, '2025-09-25 09:30:41', '2025-09-25 15:24:11', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (798, 2, NULL, 2, 2, 0, 12, '2025-09-25 17:01:52', '2025-09-25 17:21:12', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (799, 2, NULL, 2, 2, 0, 12, '2025-09-25 17:27:49', '2025-09-25 17:35:54', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (800, 34, 10, 1, 2, 0, 12, '2025-09-26 08:52:02', '2025-09-26 09:22:08', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (801, 18, NULL, 2, 2, 0, 10, '2025-09-26 13:04:27', '2025-09-26 13:28:06', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (802, NULL, 6, 1, 3, 2, 27, '2025-09-26 14:51:09', '2025-10-03 11:35:28', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (803, NULL, NULL, 1, 3, 0, 27, '2025-09-29 10:54:51', '2025-10-03 11:35:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (804, 18, NULL, 2, 2, 0, 10, '2025-09-29 12:03:32', '2025-09-29 13:01:39', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (805, NULL, 6, 1, 1, 0, 27, '2025-09-29 12:05:26', '2025-10-03 11:36:15', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (806, NULL, NULL, 1, 1, 0, 27, '2025-09-29 15:24:16', '2025-10-03 11:36:39', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (807, NULL, NULL, 1, 3, 0, 12, '2025-09-30 08:16:06', '2025-09-30 09:16:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (808, NULL, NULL, 1, 3, 0, 27, '2025-09-30 10:43:03', '2025-10-03 11:36:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (809, NULL, NULL, 1, 3, 0, 12, '2025-09-30 12:38:28', '2025-09-30 12:50:52', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (810, NULL, NULL, 1, 3, 0, 27, '2025-09-30 15:31:52', '2025-10-03 11:37:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (811, 2, NULL, 2, 2, 0, 12, '2025-09-30 15:34:07', '2025-09-30 16:41:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (818, 18, NULL, 2, 2, 0, 12, '2025-10-01 11:37:37', '2025-10-01 15:23:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (819, NULL, 1, 1, 3, 0, 12, '2025-10-01 11:40:07', '2025-10-01 15:25:57', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (820, NULL, 7, 1, 3, 1, 12, '2025-10-01 14:03:49', '2025-10-01 14:03:49', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (821, NULL, NULL, 1, 3, 0, 12, '2025-10-01 14:11:16', '2025-10-01 15:02:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (822, NULL, 6, 1, 3, 0, 12, '2025-10-01 15:35:26', '2025-10-01 18:09:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (823, 2, NULL, 2, 2, 0, 12, '2025-10-01 15:43:03', '2025-10-01 16:07:04', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (824, 25, 1, 1, 3, 0, 12, '2025-10-02 07:46:50', '2025-10-02 09:04:05', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (825, NULL, NULL, 1, 3, 0, 27, '2025-10-03 10:15:08', '2025-10-03 11:37:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (826, NULL, NULL, 1, 3, 1, 27, '2025-10-03 12:37:07', '2025-10-10 11:30:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (827, 5, NULL, 1, 3, 0, 12, '2025-10-03 15:44:05', '2025-10-04 09:22:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (828, NULL, NULL, 1, 3, 2, 12, '2025-10-03 15:45:52', '2025-10-04 10:45:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (829, NULL, 1, 1, 3, 0, 12, '2025-10-04 10:40:36', '2025-10-04 14:17:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (830, NULL, NULL, 1, 3, 0, 6, '2025-10-04 10:54:42', '2025-10-04 11:23:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (831, NULL, 1, 1, 3, 0, 12, '2025-10-04 13:58:22', '2025-10-05 08:28:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (832, 3, NULL, 2, 2, 0, 12, '2025-10-04 14:17:41', '2025-10-06 07:56:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (833, 26, 2, 1, 3, 2, 12, '2025-10-06 07:17:02', '2025-10-06 10:50:39', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (834, 18, NULL, 2, 2, 0, 10, '2025-10-06 13:03:58', '2025-10-10 11:28:20', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (835, 3, NULL, 2, 2, 0, 12, '2025-10-06 16:43:24', '2025-10-07 08:25:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (836, NULL, NULL, 1, 3, 1, 27, '2025-10-07 13:42:11', '2025-10-10 11:31:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (837, 26, 2, 1, 3, 0, 12, '2025-10-07 15:54:29', '2025-10-09 16:14:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (838, NULL, 1, 1, 3, 0, 12, '2025-10-09 10:34:20', '2025-10-09 11:33:38', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (839, 34, NULL, 1, 2, 0, 20, '2025-10-09 13:13:45', '2025-10-09 13:59:53', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (840, 35, NULL, 1, 2, 0, 12, '2025-10-09 13:55:21', '2025-10-09 14:57:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (841, NULL, NULL, 1, 3, 0, 12, '2025-10-09 16:09:09', '2025-10-10 08:45:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (842, NULL, 6, 1, 3, 2, 12, '2025-10-09 18:15:59', '2025-10-11 08:53:04', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (843, 18, NULL, 2, 2, 0, 10, '2025-10-10 09:06:26', '2025-10-10 09:38:33', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (844, 3, NULL, 2, 2, 0, 12, '2025-10-10 11:22:50', '2025-10-10 11:59:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (845, 36, NULL, 1, 2, 0, 28, '2025-10-10 11:35:09', '2025-10-10 12:53:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (846, NULL, 6, 1, 3, 0, 12, '2025-10-10 13:56:26', '2025-10-10 14:12:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (847, NULL, NULL, 1, 3, 1, 27, '2025-10-10 15:57:21', '2025-10-13 07:42:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (848, NULL, NULL, 1, 3, 1, 27, '2025-10-10 15:59:27', '2025-10-10 15:59:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (849, NULL, 1, 1, 3, 0, 12, '2025-10-11 08:40:24', '2025-10-11 10:08:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (850, 25, 1, 1, 3, 0, 12, '2025-10-11 10:09:53', '2025-10-11 14:18:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (851, 35, NULL, 1, 2, 2, 12, '2025-10-11 10:52:38', '2025-10-11 11:31:21', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (852, NULL, 6, 1, 3, 0, 12, '2025-10-11 13:48:08', '2025-10-13 08:43:34', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (853, 25, 1, 1, 3, 0, 12, '2025-10-11 14:18:11', '2025-10-13 10:37:04', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (854, 3, NULL, 2, 2, 0, 12, '2025-10-13 06:53:09', '2025-10-13 07:31:13', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (855, 35, NULL, 1, 2, 2, 12, '2025-10-15 14:01:28', '2025-10-15 15:12:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (856, 25, 1, 1, 3, 0, 12, '2025-10-16 09:05:58', '2025-10-16 09:42:33', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (857, NULL, 1, 1, 3, 0, 12, '2025-10-16 10:42:36', '2025-10-16 13:28:22', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (858, NULL, 3, 1, 3, 1, 27, '2025-10-16 11:09:17', '2025-10-22 08:25:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (859, 5, NULL, 1, 3, 0, 12, '2025-10-16 17:45:07', '2025-10-17 10:23:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (860, NULL, 1, 1, 3, 0, 12, '2025-10-17 11:51:13', '2025-10-17 15:37:45', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (861, NULL, NULL, 1, 3, 1, 27, '2025-10-17 16:27:30', '2025-10-23 12:30:10', 'S', 28);
INSERT INTO `administracion_pedidos` VALUES (862, 31, NULL, 1, 2, 0, 12, '2025-10-18 08:31:53', '2025-10-18 08:57:44', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (863, 28, NULL, 1, 3, 0, 12, '2025-10-18 09:33:16', '2025-10-18 10:25:52', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (864, 25, 1, 1, 3, 0, 12, '2025-10-18 10:22:16', '2025-10-18 11:09:24', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (865, 25, 1, 1, 3, 0, 12, '2025-10-18 13:17:12', '2025-10-18 13:59:25', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (866, 25, 1, 1, 3, 0, 12, '2025-10-20 14:19:14', '2025-10-20 15:08:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (867, 4, NULL, 1, 2, 0, 10, '2025-10-21 10:22:48', '2025-10-21 13:10:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (868, 25, 1, 1, 3, 0, 12, '2025-10-22 11:08:24', '2025-10-22 11:57:07', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (869, NULL, NULL, 1, 3, 1, 27, '2025-10-22 12:12:54', '2025-10-22 12:12:54', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (870, 4, NULL, 1, 2, 0, 10, '2025-10-22 16:27:49', '2025-10-23 14:16:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (871, 26, 2, 1, 3, 2, 12, '2025-10-22 17:21:01', '2025-10-23 12:03:48', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (872, 25, 1, 1, 3, 0, 12, '2025-10-23 13:46:00', '2025-10-23 15:00:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (873, NULL, NULL, 1, 3, 0, 12, '2025-10-24 13:28:05', '2025-10-24 14:22:34', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (874, NULL, NULL, 1, 3, 0, 12, '2025-10-24 15:43:28', '2025-10-24 16:05:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (875, 26, 2, 1, 3, 0, 12, '2025-10-24 18:08:10', '2025-10-25 12:43:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (876, 25, NULL, NULL, NULL, 0, 12, '2025-10-25 07:21:26', '2025-10-25 13:56:25', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (877, 25, NULL, 1, 3, 0, 12, '2025-10-25 07:33:01', '2025-10-25 13:48:27', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (878, 25, 1, 1, 3, 0, 12, '2025-10-25 08:52:38', '2025-10-25 10:48:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (879, 25, 1, 1, 3, 0, 12, '2025-10-25 13:47:22', '2025-10-25 13:59:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (880, NULL, NULL, 1, 3, 0, 12, '2025-10-28 16:59:13', '2025-10-29 09:09:58', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (881, NULL, NULL, 1, 3, 0, 12, '2025-10-28 17:00:40', '2025-10-29 06:48:53', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (882, 37, NULL, 1, 2, 0, 12, '2025-10-29 07:57:18', '2025-10-30 16:11:50', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (883, NULL, NULL, 1, 3, 1, NULL, '2025-10-29 12:50:06', '2025-10-29 12:50:06', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (884, NULL, NULL, 1, 3, 0, 12, '2025-10-29 12:51:41', '2025-10-29 13:02:44', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (885, NULL, NULL, 1, 3, 1, 27, '2025-10-29 15:52:54', '2025-10-30 10:20:14', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (886, NULL, NULL, 1, 3, 1, 27, '2025-10-29 15:54:32', '2025-10-29 15:54:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (887, 26, 2, 1, 3, 0, 12, '2025-10-29 17:51:50', '2025-10-30 07:56:56', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (888, 37, NULL, 1, 2, 0, 12, '2025-10-30 16:11:32', '2025-10-31 10:28:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (889, 26, 2, 1, 3, 0, 12, '2025-10-31 07:05:34', '2025-10-31 09:18:24', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (890, 5, NULL, 1, 3, 0, 12, '2025-10-31 12:15:04', '2025-10-31 14:09:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (891, NULL, NULL, 1, 3, 1, 27, '2025-10-31 12:38:20', '2025-11-05 09:33:51', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (892, NULL, NULL, 1, 3, 0, 12, '2025-10-31 16:22:02', '2025-11-03 09:28:43', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (893, NULL, NULL, 1, 3, 1, 27, '2025-11-04 09:07:17', '2025-11-04 09:07:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (894, 2, NULL, 2, 2, 0, 12, '2025-11-04 10:36:20', '2025-11-04 10:55:29', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (895, 25, 1, 1, 3, 0, 12, '2025-11-05 16:50:40', '2025-11-06 08:10:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (896, 38, NULL, 2, 2, 2, 25, '2025-11-06 07:47:57', '2025-11-06 13:56:26', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (897, 25, 1, 1, 3, 0, 12, '2025-11-06 10:34:36', '2025-11-06 13:29:17', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (898, 25, 1, 1, 3, 1, 12, '2025-11-06 11:29:16', '2025-11-06 13:36:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (899, 2, NULL, 1, 2, 0, 12, '2025-11-06 14:37:12', '2025-11-06 16:13:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (900, 38, NULL, 2, 2, 0, 18, '2025-11-07 09:59:28', '2025-11-12 13:25:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (901, 2, NULL, 1, 2, 0, 12, '2025-11-07 12:23:37', '2025-11-08 10:19:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (902, 26, 2, 1, 3, 0, 12, '2025-11-07 17:18:43', '2025-11-08 11:26:42', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (903, 25, 1, 1, 3, 0, 12, '2025-11-07 17:36:25', '2025-11-08 07:34:44', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (904, 25, 1, 1, 3, 0, 12, '2025-11-07 17:40:16', '2025-11-08 10:37:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (905, 26, 2, 1, 3, 0, 12, '2025-11-08 08:09:16', '2025-11-10 11:51:12', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (906, 40, NULL, 2, 2, 0, 20, '2025-11-08 12:15:46', '2025-11-10 07:26:41', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (907, 38, NULL, 2, 2, 0, 25, '2025-11-08 12:20:53', '2025-11-17 10:21:02', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (908, 38, NULL, 2, 2, 0, 18, '2025-11-10 10:40:20', '2025-11-10 12:13:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (909, 2, NULL, 2, 2, 2, 12, '2025-11-10 16:21:48', '2025-11-10 16:38:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (910, 18, NULL, 2, 2, 0, 10, '2025-11-10 17:31:14', '2025-11-11 08:18:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (911, 40, NULL, 2, 2, 0, 18, '2025-11-11 07:50:19', '2025-11-11 13:27:00', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (912, 38, NULL, 2, 2, 0, 20, '2025-11-11 12:06:03', '2025-11-11 14:56:20', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (913, 2, NULL, 2, 3, 0, 12, '2025-11-11 15:18:20', '2025-11-11 18:18:15', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (914, 38, NULL, 2, 2, 2, 18, '2025-11-12 11:28:17', '2025-11-18 10:45:18', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (915, 41, NULL, 1, 2, 0, 12, '2025-11-12 16:00:49', '2025-11-12 17:33:33', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (916, 41, NULL, NULL, NULL, 0, 12, '2025-11-13 05:17:05', '2025-11-17 09:34:45', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (917, 25, 1, 1, 3, 0, 12, '2025-11-13 09:49:50', '2025-11-13 12:14:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (918, 41, NULL, 1, 2, 0, 12, '2025-11-13 09:56:29', '2025-11-13 10:33:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (919, 38, NULL, 2, 2, 2, 18, '2025-11-13 10:52:51', '2025-11-13 14:28:55', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (920, 37, NULL, 1, 2, 0, 25, '2025-11-13 17:16:32', '2025-11-14 12:42:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (921, 2, NULL, 2, 2, 0, 25, '2025-11-14 08:33:17', '2025-11-14 13:19:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (922, 38, NULL, 2, 2, 0, 18, '2025-11-14 12:30:21', '2025-11-14 14:02:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (923, 2, NULL, 2, 2, 0, 25, '2025-11-14 16:17:53', '2025-11-14 16:41:31', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (924, 2, NULL, 2, 2, 0, 12, '2025-11-15 07:51:00', '2025-11-15 09:25:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (925, 25, 1, 1, 3, 0, 12, '2025-11-15 08:29:23', '2025-11-15 12:52:02', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (926, 25, 1, 1, 3, 0, 12, '2025-11-15 10:24:03', '2025-11-15 15:07:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (927, NULL, NULL, 1, 3, 1, 27, '2025-11-15 11:10:50', '2025-11-15 11:10:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (928, 18, NULL, 2, 2, 0, 12, '2025-11-17 06:30:47', '2025-11-17 10:58:34', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (929, 40, NULL, 2, 2, 0, 18, '2025-11-17 10:55:23', '2025-11-17 13:55:50', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (930, 41, NULL, 1, 2, 0, 12, '2025-11-18 08:38:13', '2025-11-18 14:14:05', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (931, 40, NULL, 2, 2, 0, 20, '2025-11-18 11:56:55', '2025-11-18 13:27:49', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (932, 41, NULL, 1, 2, 0, 12, '2025-11-18 15:47:47', '2025-11-18 16:15:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (933, 40, NULL, 2, 2, 0, 31, '2025-11-19 10:11:06', '2025-11-19 13:38:27', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (934, 2, NULL, 2, 2, 0, 12, '2025-11-19 14:19:50', '2025-11-19 15:35:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (935, 25, 1, 1, 2, 0, 12, '2025-11-20 10:08:26', '2025-11-20 13:19:37', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (936, NULL, NULL, 1, 3, 0, 10, '2025-11-20 11:36:47', '2025-11-20 12:13:47', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (937, NULL, NULL, 1, 3, 0, 6, '2025-11-20 11:56:50', '2025-11-20 13:04:32', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (938, 42, NULL, 1, 2, 1, 24, '2025-11-20 15:23:16', '2025-11-24 11:36:36', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (939, NULL, NULL, 1, 3, 0, 10, '2025-11-20 17:11:48', '2025-11-20 17:55:25', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (940, 41, NULL, 1, 2, 2, 12, '2025-11-21 05:16:39', '2025-11-21 07:38:01', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (941, 38, NULL, 2, 2, 2, 25, '2025-11-21 07:54:02', '2025-11-21 13:44:59', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (942, 41, NULL, 1, 2, 2, 12, '2025-11-21 10:13:46', '2025-11-22 08:57:35', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (943, 5, NULL, 1, 3, 2, 12, '2025-11-22 05:03:51', '2025-11-22 07:23:34', 'S', 30);
INSERT INTO `administracion_pedidos` VALUES (944, 25, 1, 1, 3, 0, 12, '2025-11-22 08:39:20', '2025-11-22 13:52:01', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (945, 41, NULL, 1, 2, 0, 12, '2025-11-22 10:16:33', '2025-11-22 11:58:08', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (946, NULL, NULL, 1, 3, 1, 27, '2025-11-22 10:44:16', '2025-11-22 10:44:16', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (947, 41, NULL, 1, 2, 0, 12, '2025-11-22 12:51:26', '2025-11-22 14:09:03', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (948, 2, NULL, 2, 2, 0, 12, '2025-11-24 08:00:57', '2025-11-24 13:22:23', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (949, 38, NULL, 2, 2, 0, 18, '2025-11-24 11:35:43', '2025-11-24 12:59:12', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (950, 38, NULL, 2, 2, 0, 25, '2025-11-24 11:55:16', '2025-11-24 13:41:40', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (951, 38, NULL, 2, 2, 0, 18, '2025-11-25 10:59:45', '2025-11-25 13:32:13', 'S', 29);
INSERT INTO `administracion_pedidos` VALUES (952, 40, NULL, 2, 2, 1, 25, '2025-11-25 14:48:23', '2025-11-25 17:20:35', 'S', 29);

-- ----------------------------
-- Table structure for administracion_programacion
-- ----------------------------
DROP TABLE IF EXISTS `administracion_programacion`;
CREATE TABLE `administracion_programacion`  (
  `id_programacion` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NULL DEFAULT NULL,
  `fecha` date NULL DEFAULT NULL,
  `dia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `hora` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `v_guia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'v_guia (Valumen Guia para el cliente): Es un valor para el cliente\r\nv_real (Volumen Real para el vendedor): Es un valor para el Vendedor\r\nv_carga(Volumen Carga para la empresa que vende): Es un valor para la empresa',
  `v_real` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'v_guia (Valumen Guia para el cliente): Es un valor para el cliente\r\nv_real (Volumen Real para el vendedor): Es un valor para el Vendedor\r\nv_carga(Volumen Carga para la empresa que vende): Es un valor para la empresa',
  `v_carga` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'v_guia (Valumen Guia para el cliente): Es un valor para el cliente\r\nv_real (Volumen Real para el vendedor): Es un valor para el Vendedor\r\nv_carga(Volumen Carga para la empresa que vende): Es un valor para la empresa',
  `por_cargar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  `ubicacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `comentario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_tipo_bomba` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `address_id_departamento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CODGO DEPARTAMENTO',
  `address_id_provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CODIGO PROVINCIA',
  `address_ubigueo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CODIGO DISTRITO O UBIGUEO',
  `address_departamento` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'NOMBRE DEL DEPARTAMENTO',
  `address_provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'NOMBRE DE LA PROVINCIA',
  `address_distrito` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'NOMBRE DEL DISTRITO',
  `address_direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'DIRECCION O LUGAR DE RESIDENCIA',
  `v_guia_modificado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'v_guia (Valumen Guia): Este es un campo null cuando se llena por primera vez, significa que ya esta siendose la Guia de remision, y se esta procediendo al carguio. Cuando llega a cero significa que las cargas fueron enviadas.',
  `unidad_medida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_programacion`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 953 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_programacion
-- ----------------------------
INSERT INTO `administracion_programacion` VALUES (2, 2, '2025-03-17', 'Lunes', '2:00 PM', '9,00', '9,00', '8.5', '-2.5', 'PRUEBA1,LIMA-LIMA -ATE', NULL, 2, '2025-03-18 11:39:38', '2025-07-02 12:36:36', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'PRUEBA1', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (3, 3, '2025-03-17', 'Lunes', '2:30 PM', '9,00', '9,00', '0', '0', NULL, NULL, NULL, '2025-03-18 11:46:34', '2025-03-18 11:46:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (4, 4, '2025-03-18', 'Martes', '2:00 PM', '21.5', '21.5', '21', '21', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-03-18 11:49:56', '2025-03-19 11:07:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (5, 5, '2025-03-17', 'Lunes', '2:50 PM', '9,00', '9,00', '0', '0', NULL, 'SLUMP 5', NULL, '2025-03-18 11:50:31', '2025-03-18 11:50:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (6, 6, '2025-03-18', 'Martes', '2:30 PM', '7.00', '7.00', '6.75', '6.75', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 7', 5, '2025-03-18 11:51:19', '2025-03-19 11:07:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (7, 7, '2025-03-18', 'Martes', '4:00 PM', '9.00', '9.00', '8.75', '8.75', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', NULL, 6, '2025-03-18 11:52:52', '2025-03-19 11:07:58', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (8, 8, '2025-03-19', 'Miércoles', '1:00 PM', '24.00', '24.00', '23.5', '0', 'EL CONDOMINIO DE ENSUEÑO, LIMA - HUAROCHIRI - RICARDO PALMA', 'SLUMP 4-6', 6, '2025-03-19 10:18:13', '2025-03-19 12:51:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (9, 9, '2025-03-19', 'Miércoles', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA-LIMA-JESUS MARIA', 'SLUMP 7', 5, '2025-03-19 10:21:51', '2025-03-19 13:08:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (10, 10, '2025-03-19', 'Miércoles', '2:00 PM', '10.50', '10.50', '10.25', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 5, '2025-03-19 10:24:11', '2025-03-19 13:51:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (11, 11, '2025-03-19', 'Miércoles', '2:00 PM', '6.00', '6.00', '5.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 2, '2025-03-19 10:28:17', '2025-03-19 13:27:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (12, 12, '2025-03-19', 'Miércoles', '4:00 PM', '9.00', '9.00', '8.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA-LIMA-LURIGANCHO', 'SLUMP 8', 5, '2025-03-19 10:39:54', '2025-03-19 18:16:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (13, 13, '2025-03-19', 'Miércoles', '2:30 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA -LIMA-JESUS MARIA', 'SLUMP 7', 5, '2025-03-19 11:55:35', '2025-03-19 14:16:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (14, 14, '2025-03-20', 'Jueves', '1:00 PM', '28', '28', '27.5', '0', 'AV GRAL. SANTA CRUZ 708 , ESQUINA CON JIRON PUMACAHUA , LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 6, '2025-03-19 17:50:06', '2025-03-20 13:25:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (34, 34, '2025-03-20', 'Jueves', '2:30 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA-JESUS MARIA', 'SLUMP 7', 5, '2025-03-19 18:12:18', '2025-03-20 13:46:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (35, 35, '2025-03-20', 'Jueves', '1:00 PM', '5.00', '5.00', '4.75', '0', 'AV CUBA C/ AV. SALAVERRY, JESUS MARIA - LIMA', 'SLUMP 6-8', 2, '2025-03-19 18:36:49', '2025-03-20 12:18:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (36, 36, '2025-03-20', 'Jueves', '10:00 AM', '6.00', '6.00', '5.75', '0', 'JR INCA RIPAC 326', 'SLUMP 6-8', 5, '2025-03-19 18:46:00', '2025-03-20 09:08:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (37, 37, '2025-03-20', 'Jueves', '4:00 PM', '8.00', '8.00', '7.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA- LIMA-LURIGANCHO', 'SLUMP 8', 6, '2025-03-20 08:42:03', '2025-03-20 17:15:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (38, 38, '2025-03-21', 'Viernes', '2:00 PM', '8.00', '8.00', '7.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON PUMACAHUA , LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 2, '2025-03-20 16:47:29', '2025-04-11 08:26:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (39, 39, '2025-03-21', 'Viernes', '2:30 PM', '10.00', '10.00', '9.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA-LIMA -JESUS MARIA', 'SLUMP 7', 5, '2025-03-20 17:05:22', '2025-03-21 13:45:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (40, 40, '2025-03-21', 'Viernes', '3:00 PM', '8.00', '8.00', '7.75', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA-LURIGANCHO', 'SLUMP 6', 5, '2025-03-20 17:09:24', '2025-03-21 14:02:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (41, 41, '2025-03-22', 'Sábado', '12:30 PM', '20.00', '20.00', '19.75', '0', 'PARC RUST CAJAMARQUILLA PARCELA NRO 1 LOTE 1C LURIGANCHO - LIMA - LIMA', 'SLUMP 6-8', 2, '2025-03-21 16:37:59', '2025-03-22 14:05:16', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (42, 42, '2025-03-22', 'Sábado', '12:00 PM', '13.00', '10.5', '10.25', '0', 'CERRO CAMOTE', 'SLUMP 4-6', 2, '2025-03-21 17:18:14', '2025-03-22 13:38:09', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (43, 43, '2025-03-22', 'Sábado', '6:00 AM', '12.00', '10.5', '10.25', '0', 'VALLE GRANDE', 'SLUMP 6-8', 2, '2025-03-21 18:48:10', '2025-03-22 09:20:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (44, 44, '2025-03-22', 'Sábado', '8:00 AM', '11.00', '9.50', '9.25', '0', 'SANTA CLARA', NULL, 2, '2025-03-22 07:59:50', '2025-03-22 08:28:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (45, 45, '2025-03-22', 'Sábado', '9:00 AM', '15.00', '13.00', '12.75', '0', 'VALLE GRANDE', NULL, 2, '2025-03-22 08:28:21', '2025-03-22 09:09:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (47, 47, '2025-03-22', 'Sábado', '10:00 AM', '13.00', '10.00', '9.75', '0', 'LAS AMERICAS', NULL, 2, '2025-03-22 08:59:14', '2025-03-22 12:43:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (48, 48, '2025-03-24', 'Lunes', '1:30 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA-JESUS MARIA', 'SLUMP 7', 5, '2025-03-22 14:02:50', '2025-03-24 12:50:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (49, 49, '2025-03-24', 'Lunes', '2:00 PM', '14.00', '14.00', '13.50', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA- JESUS MARIA', 'SLUMP 6', 6, '2025-03-22 14:04:22', '2025-03-24 14:12:15', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (50, 50, '2025-03-24', 'Lunes', '1:00 PM', '45.00', '45.00', '44.50', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON PUMACAHUA , LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 6, '2025-03-24 10:09:36', '2025-03-24 14:34:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (51, 51, '2025-03-24', 'Lunes', '4:30 PM', '11.00', '11.00', '10.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA- LIMA-LURIGANCHO', 'SLUMP 8', 6, '2025-03-24 10:11:23', '2025-03-24 17:48:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (52, 52, '2025-03-25', 'Martes', '2:30 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217, LIMA-LIMA- JESUS MARIA', 'SLUMP 7', 6, '2025-03-24 16:59:43', '2025-03-25 13:38:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (53, 53, '2025-03-25', 'Martes', '10:30 AM', '29.00', '29.00', '28.50', '0', 'AV. GRAL. SANTA CRUZ 708 ESQUINA CON JR PUMACAHUA', 'SLUMP 6', 6, '2025-03-24 17:05:00', '2025-03-25 11:07:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (54, 54, '2025-03-25', 'Martes', '2:30 PM', '13.00', '13.00', '12.75', '0', 'AV. GRAL. SANTA CRUZ 708. ESQUINA CON JR PUMACHAHUA', 'SLUMP 6-8', 2, '2025-03-24 17:06:52', '2025-03-25 15:06:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (55, 55, '2025-03-25', 'Martes', '4:30 PM', '7.00', '7.00', '6.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 8', 6, '2025-03-25 09:51:15', '2025-03-25 16:58:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (57, 57, '2025-03-25', 'Martes', '4:00 PM', '9.00', '8.5', '8.25', '0', 'CAMPOY', 'SLUMP 6-8', 2, '2025-03-25 16:14:50', '2025-03-25 16:38:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (58, 58, '2025-03-26', 'Miércoles', '2:30 PM', '9.5', '9.5', '9.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA-JESUS MARIA', 'SLUMP 7', 6, '2025-03-25 17:21:50', '2025-03-26 13:24:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (59, 59, '2025-03-26', 'Miércoles', '3:00 PM', '11.00', '11.00', '10.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 6, '2025-03-25 17:24:45', '2025-03-26 13:45:43', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (60, 60, '2025-03-26', 'Miércoles', '12:30 PM', '18.00', '18.00', '17.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 6, '2025-03-26 08:33:55', '2025-03-26 11:52:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (61, 61, '2025-03-27', 'Jueves', '1:00 PM', '28.5', '28.5', '28', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-03-26 16:04:46', '2025-03-27 13:08:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (62, 62, '2025-03-27', 'Jueves', '2:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA - JESUS MARIA', 'SLUMP 7', 6, '2025-03-26 17:42:30', '2025-03-27 13:26:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (63, 63, '2025-03-27', 'Jueves', '2:30 PM', '10.00', '10.00', '9.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 6, '2025-03-26 17:45:11', '2025-03-27 13:49:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (66, 66, '2025-03-28', 'Viernes', '8:00 AM', '6.00', '6.00', '5.75', '0', 'RICARDO PALMA-CHOSICA', 'SLUMP 4-6', 7, '2025-03-27 14:56:23', '2025-03-28 07:00:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (67, 67, '2025-03-27', 'Jueves', '4:30 PM', '9.5', '9.5', '9.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 6-8 OBRA BALANZA NAVE I5 - ACH', 2, '2025-03-27 15:04:59', '2025-03-27 17:16:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (68, 68, '2025-03-28', 'Viernes', '2:20 PM', '14.5', '14.5', '14.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA LIMA- JESUS MARIA', 'SLUMP 6', 6, '2025-03-27 18:08:34', '2025-03-29 12:20:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (70, 70, '2025-03-28', 'Viernes', '12:00 PM', '45.5', '45.5', '45', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-03-27 18:12:28', '2025-03-28 12:57:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (71, 71, '2025-03-28', 'Viernes', '1:50 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA LIMA- JESUS MARIA', 'SLUMP 7', 6, '2025-03-28 11:54:49', '2025-03-28 13:21:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (72, 72, '2025-03-28', 'Viernes', '4:30 PM', '32.00', '32.00', '27.75', '0', 'AV LOS TUCANES S/N SANTAA MARIA DE HUACHIPA LIMA LIMA LURIGANCHO', 'SLUMP 8 CW BALANZA I5', 2, '2025-03-28 12:19:04', '2025-03-28 17:46:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (73, 73, '2025-03-29', 'Sábado', '10:00 AM', '22.50', '19.50', '19.25', '0', 'CERRO CAMOTE - LAS LOMAS', 'SLUMP 6-8', 2, '2025-03-28 15:34:01', '2025-03-29 11:14:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (74, 74, '2025-03-29', 'Sábado', '8:00 AM', '7.00', '7.00', '6.75', '0', 'HUACHIPA', 'EN GUIA DISEÑO 210', 2, '2025-03-28 18:51:18', '2025-03-29 08:35:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (75, 75, '2025-03-29', 'Sábado', '2:00 PM', '54.00', '54.00', '53', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 8 NAVE J1 - ACH', 2, '2025-03-29 09:08:13', '2025-03-29 16:21:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (76, 76, '2025-03-31', 'Lunes', '12:00 PM', '44.5', '44.5', '43.00', '6.25', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA JESUS MARIA', NULL, 6, '2025-03-29 10:44:49', '2025-04-01 11:21:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (79, 79, '2025-03-31', 'Lunes', '10:30 AM', '8.5', '8.5', '8.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA LIMA- JESUS MARIA', 'SLUMP 6', 6, '2025-03-29 11:39:52', '2025-03-31 09:38:15', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (80, 80, '2025-03-29', 'Sábado', '1:00 PM', NULL, NULL, '0', '0', NULL, NULL, 6, '2025-03-31 08:39:48', '2025-03-31 08:39:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (81, 81, '2025-03-29', 'Sábado', '2:00 PM', NULL, NULL, '0', '0', NULL, NULL, 6, '2025-03-31 08:42:52', '2025-03-31 08:42:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (82, 82, '2025-03-31', 'Lunes', '2:00 PM', NULL, NULL, '0', '0', 'BREÑA', 'SERVICIO DE BOMBA', 6, '2025-03-31 08:51:00', '2025-03-31 08:51:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (83, 83, '2025-03-29', 'Sábado', '10:00 AM', '1', '1', '1', '0', 'CALLE ARAGÓN 390 PUEBLO LIBRE', NULL, 6, '2025-03-31 08:53:26', '2025-03-31 09:06:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (84, 84, '2025-03-29', 'Sábado', '10:00 AM', '1', '1', '1', '0', 'SAN JUAN DE LURIGANCHO', NULL, 5, '2025-03-31 08:56:17', '2025-03-31 10:04:09', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (85, 85, '2025-03-29', 'Sábado', '2:00 PM', '1', '1', '1', '0', 'SAN JUAN DE LURIGANCHO', NULL, 5, '2025-03-31 08:59:31', '2025-03-31 09:07:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (86, 86, '2025-03-31', 'Lunes', '11:00 AM', '47.5', '47.5', '46.5', '8', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1 ACH', 2, '2025-03-31 09:36:54', '2025-03-31 18:23:03', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (87, 87, '2025-03-31', 'Lunes', '4:00 PM', '21.00', '21.00', '20.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1 ACH', 2, '2025-03-31 09:38:50', '2025-03-31 18:50:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (88, 88, '2025-04-01', 'Martes', '4:00 PM', '17.00', '17.00', '16.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 8  NAVE J1 ACH', 2, '2025-03-31 09:49:58', '2025-04-01 19:51:39', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (89, 89, '2025-04-01', 'Martes', '10:30 AM', '8.00', '8.00', '7.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 7', 4, '2025-03-31 18:00:52', '2025-04-02 09:01:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (90, 90, '2025-04-01', 'Martes', '10:50 AM', '21.00', '21.00', '20.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-03-31 18:02:33', '2025-04-02 09:01:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (91, 91, '2025-04-01', 'Martes', '2:30 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-03-31 18:03:49', '2025-04-02 09:01:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (92, 92, '2025-04-01', 'Martes', '3:30 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-03-31 19:06:59', '2025-04-01 15:07:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (93, 93, '2025-04-01', 'Martes', '1:00 PM', '35.50', '35.50', '35', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-03-31 19:07:57', '2025-04-01 14:27:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (95, 95, '2025-04-01', 'Martes', '8:00 AM', '61.50', '61.50', '61', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP6-8 NAVE J1 ACH', 2, '2025-04-01 08:45:36', '2025-04-01 17:19:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (96, 96, '2025-04-01', 'Martes', '4:00 PM', '14.00', '14.00', '13.75', '0.5', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 4-6  CW BALANZA I5', 2, '2025-04-01 09:12:06', '2025-04-01 19:01:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (97, 97, '2025-04-01', 'Martes', '4:30 PM', '10.50', '10.50', '10.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1 ACH', 7, '2025-04-01 09:15:47', '2025-04-01 18:29:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (98, 98, '2025-04-01', 'Martes', '10:00 AM', '8.00', '8.00', '7.75', '0', 'PARC RUST CAJAMARQUILLA LOTE. 1C PARCELA NRO 1', '280 C/ACELERANTE A 3 DIAS    - SLUMP 6\"-8\"', 2, '2025-04-01 09:50:39', '2025-04-01 14:49:24', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (100, 100, '2025-04-02', 'Miércoles', '1:00 PM', '15.5', '15.5', '15.25', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-04-02 08:56:30', '2025-04-02 13:03:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (101, 101, '2025-04-02', 'Miércoles', '9:00 AM', '47.00', '47.00', '46.5', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'NAVE J1', 2, '2025-04-02 09:00:41', '2025-04-03 14:23:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (102, 102, '2025-04-02', 'Miércoles', '9:00 AM', '8.00', '8.00', '7.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', '+ ANTISALITRE 6\"', 2, '2025-04-02 09:11:04', '2025-04-02 10:54:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (103, 103, '2025-04-02', 'Miércoles', '3:00 PM', '80.00', '80.00', '79.5', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 8', 2, '2025-04-02 11:21:07', '2025-04-02 20:07:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (104, 104, '2025-04-02', 'Miércoles', '5:00 PM', '20.00', '20.00', '19.75', '5.75', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'NAVE J1', 6, '2025-04-02 17:12:31', '2025-04-02 18:41:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (105, 105, '2025-04-03', 'Jueves', '1:00 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-02 17:59:42', '2025-04-03 14:44:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (106, 106, '2025-04-03', 'Jueves', '2:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-02 18:00:39', '2025-04-03 14:44:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (107, 107, '2025-04-03', 'Jueves', '2:30 PM', '9.00', '9.00', '8.75', '-8.75', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-02 18:01:36', '2025-04-03 14:44:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (110, 110, '2025-04-03', 'Jueves', '3:30 PM', '9.50', '9.50', '9.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA,LIMA-LURIGANCHO', 'NAVE J1 SLUMP 8', 6, '2025-04-03 08:34:51', '2025-04-03 16:00:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (111, 111, '2025-04-03', 'Jueves', '4:00 PM', '17.50', '17.50', '17.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA,LIMA-LURIGANCHO', 'NAVE J1 SLUMP 8', 2, '2025-04-03 08:36:10', '2025-04-03 18:34:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (112, 112, '2025-04-03', 'Jueves', '10:00 AM', '10.00', '7.5', '7.25', '0', 'CERRO CAMOTE', NULL, 2, '2025-04-03 08:38:32', '2025-04-03 11:39:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (113, 113, '2025-04-03', 'Jueves', '3:00 PM', '12.00', '12.00', '11.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', '+ ANTISALITRE 6\"', 2, '2025-04-03 10:23:51', '2025-04-03 15:17:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (114, 114, '2025-04-04', 'Viernes', '9:30 PM', '11.5', '11.5', '11.25', '0', 'AV CUBA N° 538-528', 'SLUMP 4-6', 2, '2025-04-03 12:32:32', '2025-04-04 08:34:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (115, 115, '2025-04-04', 'Viernes', '11:30 PM', '38.5', '38.5', '38', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-04-03 17:06:59', '2025-04-04 12:04:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (116, 116, '2025-04-04', 'Viernes', '3:00 PM', '6.50', '6.50', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 5, '2025-04-03 17:20:58', '2025-04-04 15:04:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (117, 117, '2025-04-04', 'Viernes', '1:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 5, '2025-04-03 17:21:52', '2025-04-04 13:02:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (118, 118, '2025-04-04', 'Viernes', '1:30 PM', '15.00', '15.00', '14.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 5, '2025-04-03 17:23:00', '2025-04-04 14:01:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (119, 119, '2025-04-04', 'Viernes', '3:30 PM', '9.5', '9.5', '9.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'NAVE J1 SLUMP 8', 6, '2025-04-04 08:30:28', '2025-04-04 16:04:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (120, 120, '2025-04-04', 'Viernes', '4:30 PM', '17.5', '17.5', '17.25', '5', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'NAVE J1 SLUMP 8', 2, '2025-04-04 08:31:26', '2025-04-07 12:28:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (121, 121, '2025-04-04', 'Viernes', '4:00 PM', '8.00', '8.00', '7.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'BALANZA SLUMP 8', 2, '2025-04-04 08:33:02', '2025-04-04 16:32:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (122, 122, '2025-04-05', 'Sábado', '12:00 PM', '19.00', '19.00', '18.75', '0', 'CERRO CAMOTE', 'SLUMP 6-8', 6, '2025-04-04 12:11:34', '2025-04-05 13:20:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (123, 123, '2025-04-07', 'Lunes', '2:40 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-05 11:21:44', '2025-04-07 15:42:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (124, 124, '2025-04-07', 'Lunes', '1:30 PM', '8.00', '8.00', '7.75', '1', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-05 11:23:41', '2025-04-07 15:42:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (125, 125, '2025-04-07', 'Lunes', '2:00 PM', '15.5', '15.5', '15.25', '-9', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-05 11:24:47', '2025-04-07 15:42:37', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (126, 126, '2025-04-07', 'Lunes', '11:30 PM', '44.5', '44.5', '54', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-04-05 11:26:00', '2025-04-08 09:29:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (127, 127, '2025-04-07', 'Lunes', '11:30 AM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, NULL, '2025-04-07 10:32:21', '2025-04-07 11:04:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (128, 128, '2025-04-07', 'Lunes', '4:00 PM', '7.00', '7.00', '6.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 BALANZA', 2, '2025-04-07 12:19:29', '2025-04-08 08:44:24', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (129, 129, '2025-04-07', 'Lunes', '3:30 PM', '9.5', '9.5', '0', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8  NAVE J1', 4, '2025-04-07 12:20:45', '2025-04-07 16:48:15', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (130, 130, '2025-04-07', 'Lunes', '3:30 PM', '23.00', '23.00', '22.75', '-8', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-07 12:21:57', '2025-04-07 19:08:24', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (132, 132, '2025-04-08', 'Martes', '3:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-07 17:07:23', '2025-04-08 15:56:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (133, 133, '2025-04-08', 'Martes', '2:00 PM', '13.00', '13.00', '12.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-07 17:08:16', '2025-04-08 13:53:14', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (134, 134, '2025-04-08', 'Martes', '1:30 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-07 17:09:25', '2025-04-08 12:48:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (135, 135, '2025-04-08', 'Martes', '12:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-07 18:00:10', '2025-04-08 11:35:07', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (136, 136, '2025-04-08', 'Martes', '12:30 PM', '43.00', '43.00', '42', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-07 18:02:13', '2025-04-08 14:20:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (138, 138, '2025-04-08', 'Martes', '3:00 PM', '16.00', '16.00', '13.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8', 2, '2025-04-08 11:57:06', '2025-04-09 12:58:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (139, 139, '2025-04-08', 'Martes', '4:00 PM', '11.50', '11.50', '11.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8', 2, '2025-04-08 11:58:10', '2025-04-08 18:27:15', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (140, 140, '2025-04-08', 'Martes', '3:30 PM', '6.50', '6.50', '6.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8', 5, '2025-04-08 11:59:04', '2025-04-08 18:42:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (141, 141, '2025-04-09', 'Miércoles', '7:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', 'SLUMP 6', 2, '2025-04-08 16:36:55', '2025-04-09 07:29:14', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (142, 142, '2025-04-09', 'Miércoles', '1:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-08 18:34:58', '2025-04-09 11:59:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (143, 143, '2025-04-09', 'Miércoles', '1:30 PM', '18.00', '18.00', '17.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-08 18:35:56', '2025-04-09 12:42:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (144, 144, '2025-04-09', 'Miércoles', '2:00 PM', '31.00', '31.00', '30.5', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-09 11:40:22', '2025-04-09 16:39:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (145, 145, '2025-04-09', 'Miércoles', '4:00 PM', '6.50', '6.50', '6.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-09 11:41:19', '2025-04-09 17:23:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (146, 146, '2025-04-09', 'Miércoles', '3:30 PM', '6.50', '6.50', '6.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 5, '2025-04-09 11:42:10', '2025-04-09 17:04:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (147, 147, '2025-04-10', 'Jueves', '1:00 PM', '5.5', '5.5', '5.25', '-5.25', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 6, '2025-04-09 16:41:26', '2025-04-10 11:54:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (148, 148, '2025-04-10', 'Jueves', '1:30 PM', '9.00', '9.00', '8.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 6, '2025-04-09 16:42:26', '2025-04-10 12:54:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (149, 149, '2025-04-10', 'Jueves', '2:30 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 6, '2025-04-09 16:43:15', '2025-04-10 14:21:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (151, 151, '2025-04-10', 'Jueves', '2:00 PM', '13.00', '13.00', '12.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8  NAVE J1', 2, '2025-04-09 18:37:05', '2025-04-10 17:14:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (153, 153, '2025-04-10', 'Jueves', '1:00 PM', '37.00', '37.00', '36', '2', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-10 09:39:55', '2025-04-10 14:18:09', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (154, 154, '2025-04-10', 'Jueves', '2:30 PM', '7.00', '7.00', '6.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 6, '2025-04-10 14:18:26', '2025-04-11 07:30:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (155, 155, '2025-04-11', 'Viernes', '8:00 AM', '13.00', '13.00', '12.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 BALANZA', 2, '2025-04-10 16:09:59', '2025-04-11 11:16:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (156, 156, '2025-04-11', 'Viernes', '3:00 PM', '8.50', '8.50', '8.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-10 16:16:28', '2025-04-15 09:30:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (157, 157, '2025-04-11', 'Viernes', '3:30 PM', '7.00', '7.00', '6.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 5, '2025-04-10 16:17:15', '2025-04-11 18:38:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (158, 158, '2025-04-11', 'Viernes', '1:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA -LIMA-JESUS MARIA', 'SLUMP 6', 4, '2025-04-10 17:49:31', '2025-04-11 12:29:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (160, 160, '2025-04-11', 'Viernes', '2:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA -LIMA-JESUS MARIA', 'SLUMP 8', 4, '2025-04-10 17:51:32', '2025-04-11 14:20:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (161, 161, '2025-04-11', 'Viernes', '8:30 AM', '4', '4', '4', '0', 'NIEVERIA', 'SLUMP 4-6', 2, '2025-04-10 18:18:10', '2025-04-11 09:22:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (162, 162, '2025-04-11', 'Viernes', '11:00 AM', '10.00', '10.00', '0', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON PUMACAHUA , LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-04-11 08:25:45', '2025-04-11 08:25:45', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (163, 163, '2025-04-11', 'Viernes', '1:30 PM', '12.5', '12.5', '12.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 , LIMA -LIMA-JESUS MARIA', 'SLUMP 6', 4, '2025-04-11 08:32:36', '2025-04-11 13:25:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (164, 164, '2025-04-11', 'Viernes', '1:00 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 5, '2025-04-11 08:40:59', '2025-04-11 12:11:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (165, 165, '2025-04-12', 'Sábado', '10:00 AM', '8.00', '8.00', '7.75', '0', 'PARC RUST CAJAMARQUILLA PARCELA NRO 1 LOTE 1C LURIGANCHO - LIMA - LIMA', 'SLUMP 6-8', 2, '2025-04-11 09:39:59', '2025-04-12 10:38:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (166, 166, '2025-04-11', 'Viernes', '1:30 PM', '10.50', '10.50', '10.25', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-04-11 09:58:58', '2025-04-11 12:49:03', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (167, 167, '2025-04-11', 'Viernes', '2:00 PM', '7.00', '7.00', '6.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'DESPACHO POR GARANTIA', 2, '2025-04-11 11:28:32', '2025-04-11 14:07:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (168, 168, '2025-04-11', 'Viernes', '4:30 PM', '18.00', '16.00', '15.75', '0', 'NIEVERIA', NULL, 2, '2025-04-11 12:04:23', '2025-04-11 18:17:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (169, 169, '2025-04-12', 'Sábado', '7:30 PM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-04-11 12:53:45', '2025-04-12 07:55:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (170, 170, '2025-04-12', 'Sábado', '1:30 PM', '22.50', '20.50', '20.25', '-9.25', 'CERRO CAMOTE', NULL, 6, '2025-04-11 18:51:19', '2025-04-16 17:09:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (171, 171, '2025-04-14', 'Lunes', '3:00 PM', '8.50', '8.50', '8.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 6, '2025-04-12 10:33:26', '2025-04-14 14:25:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (172, 172, '2025-04-14', 'Lunes', '2:00 PM', '15.00', '15.00', '14.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 6, '2025-04-12 10:34:11', '2025-04-14 14:04:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (173, 173, '2025-04-14', 'Lunes', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 6, '2025-04-12 10:34:58', '2025-04-14 12:35:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (174, 174, '2025-04-12', 'Sábado', '10:30 AM', '3.00', '2.00', '2', '0', 'MEDIA LUNA', NULL, NULL, '2025-04-12 10:57:12', '2025-04-12 11:19:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (176, 176, '2025-04-14', 'Lunes', '12:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', 'SLUMP 6', 6, '2025-04-14 10:53:42', '2025-04-14 11:28:39', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (177, 177, '2025-04-14', 'Lunes', '1:00 PM', '20.00', '20.00', '19.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'SLUMP 4-6', 6, '2025-04-14 10:55:10', '2025-04-14 13:05:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (178, 178, '2025-04-14', 'Lunes', '4:00 PM', '11.00', '11.00', '10.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8', 2, '2025-04-14 16:09:14', '2025-04-14 16:38:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (179, 179, '2025-04-14', 'Lunes', '4:30 PM', '8.50', '8.50', '8.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8', 6, '2025-04-14 16:10:44', '2025-04-14 17:52:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (180, 180, '2025-04-15', 'Martes', '4:00 PM', '9.50', '9.50', '9.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-15 09:32:33', '2025-04-15 16:29:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (181, 181, '2025-04-15', 'Martes', '4:00 PM', '17.50', '17.50', '17.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'CON FIBRA METALICA CN/ACELERANTESLUMP 6-8 FRECUENCIA DE MIXER CADA 45 MIN', 2, '2025-04-15 09:35:46', '2025-04-15 15:43:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (182, 182, '2025-04-15', 'Martes', '11:30 AM', '18.50', '18.50', '26.5', '8.25', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'FREC 40 MIN', 6, '2025-04-15 09:38:45', '2025-04-15 13:26:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (183, 183, '2025-04-15', 'Martes', '11:30 AM', '8.50', '8.50', '8.25', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'FREC CADA 40 MIN', 6, '2025-04-15 09:40:32', '2025-04-15 12:41:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (184, 184, '2025-04-15', 'Martes', '1:30 PM', '6.50', '6.50', '6.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 7', 6, '2025-04-15 12:04:05', '2025-04-15 12:23:16', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (186, 186, '2025-04-15', 'Martes', '2:00 PM', '12.5', '12.5', '12.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP  6', 6, '2025-04-15 12:05:25', '2025-04-15 13:04:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (187, 187, '2025-04-15', 'Martes', '3:00 PM', '7.5', '7.5', '7.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 6, '2025-04-15 12:06:22', '2025-04-15 13:46:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (189, 189, '2025-04-16', 'Miércoles', '8:00 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 2, '2025-04-15 18:02:36', '2025-04-16 07:10:43', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (190, 190, '2025-04-16', 'Miércoles', '1:00 PM', '25.00', '25.00', '24.25', '-0.5', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-04-15 18:03:51', '2025-04-16 12:52:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (191, 191, '2025-04-16', 'Miércoles', '1:30 PM', '20.00', '20.00', '26.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQ CON JIRON PUMACAHUA, JESUS MARIA - LIMA', NULL, 6, '2025-04-15 18:06:06', '2025-04-16 17:08:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (192, 192, '2025-04-16', 'Miércoles', '2:30 PM', '9.00', '9.00', '8.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 5, '2025-04-15 18:07:39', '2025-04-16 14:15:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (193, 193, '2025-04-16', 'Miércoles', '9:00 AM', '16.00', '16.00', '15.75', '0', 'AV. LOS TUCANES ACH - SANTA MARIA DE HUACHIPA - LURIGANCHO', 'SLUMP 6-8', 2, '2025-04-15 18:15:46', '2025-04-16 11:45:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (194, 194, '2025-04-16', 'Miércoles', '6:00 AM', '17.00', '17.00', '16.75', '0', 'AV. LOS TUCANES ACH - SANTA MARIA DE HUACHIPA - LURIGANCHO', 'SLUMP 4', NULL, '2025-04-16 05:58:36', '2025-04-16 06:48:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (195, 195, '2025-04-16', 'Miércoles', '4:00 PM', '7.00', '7.00', '6.75', '6.75', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1 - FUE CARRO DE MONET', 2, '2025-04-16 08:48:43', '2025-04-16 17:09:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (196, 196, '2025-04-16', 'Miércoles', '4:30 PM', '12.50', '12.50', '12.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE J1', 2, '2025-04-16 08:49:51', '2025-04-16 18:05:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (197, 197, '2025-04-16', 'Miércoles', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 6, '2025-04-16 09:40:42', '2025-04-16 13:06:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (198, 198, '2025-04-21', 'Lunes', '5:00 PM', '14.00', '14.00', '13.75', '0', NULL, '210T1H67 A 28 DIAS', 6, '2025-04-16 17:02:13', '2025-04-21 18:37:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (199, 199, '2025-04-17', 'Jueves', '8:00 AM', '6.00', '6.00', '5.75', '0', 'CAJAMARQUILLA', 'SLUMP 4-6', NULL, '2025-04-17 08:24:39', '2025-04-17 08:56:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (200, 200, '2025-04-17', 'Jueves', '10:00 AM', '12.00', '12.00', '11.75', '0', 'CAJAMARQUILLA', 'SLUMP 4-6', NULL, '2025-04-17 09:12:55', '2025-04-17 12:11:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (201, 201, '2025-04-19', 'Sábado', '11:00 AM', '21.5', '18.5', '18.25', '0', 'CERRO CAMOTE -VALLE', 'SLUMP 6-8, SE ATENDIO 17.5 SE COBRARA S/4000', 2, '2025-04-19 09:23:21', '2025-04-25 12:02:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (202, 202, '2025-04-21', 'Lunes', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-19 11:43:25', '2025-04-21 12:46:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (203, 203, '2025-04-21', 'Lunes', '2:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-19 11:44:22', '2025-04-21 13:02:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (204, 204, '2025-04-21', 'Lunes', '3:00 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-19 11:45:15', '2025-04-21 14:28:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (206, 206, '2025-04-21', 'Lunes', '1:00 PM', '9.00', '9.00', '8.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-21 08:44:22', '2025-04-21 12:29:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (207, 207, '2025-04-21', 'Lunes', '3:00 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'SLUMP 6', 6, '2025-04-21 09:24:29', '2025-04-21 14:12:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (208, 208, '2025-04-21', 'Lunes', '4:00 PM', '9.00', '9.00', '8.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1', 2, '2025-04-21 10:14:49', '2025-04-21 18:18:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (209, 209, '2025-04-22', 'Martes', '8:00 AM', '16.5', '16.5', '16', '0', 'NIEVERIA', NULL, 6, '2025-04-21 11:47:56', '2025-04-22 09:12:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (210, 210, '2025-04-22', 'Martes', '4:00 PM', '21.5', '21.5', '21.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1', 2, '2025-04-21 14:31:24', '2025-04-22 16:56:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (211, 211, '2025-04-22', 'Martes', '8:00 AM', '12.00', '12.00', '11.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', NULL, '2025-04-22 07:48:14', '2025-04-22 08:17:49', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (212, 212, '2025-04-22', 'Martes', '1:00 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP  6', 4, '2025-04-22 08:51:24', '2025-04-22 12:08:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (213, 213, '2025-04-22', 'Martes', '1:30 PM', '11.5', '11.5', '11.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-22 08:52:37', '2025-04-22 12:52:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (214, 214, '2025-04-22', 'Martes', '2:00 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-22 08:53:24', '2025-04-22 13:44:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (215, 215, '2025-04-22', 'Martes', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, NULL, '2025-04-22 08:55:21', '2025-04-22 12:31:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (216, 216, '2025-04-22', 'Martes', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, NULL, '2025-04-22 08:56:03', '2025-04-22 11:53:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (217, 217, '2025-04-22', 'Martes', '2:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, NULL, '2025-04-22 08:57:53', '2025-04-22 13:28:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (218, 218, '2025-04-22', 'Martes', '2:30 PM', '6.5', '6.5', '6.25', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, NULL, '2025-04-22 10:03:06', '2025-04-22 14:05:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (219, 219, '2025-04-23', 'Miércoles', '4:30 PM', '10.00', '10.00', '9.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8  NAVE J1', 2, '2025-04-22 10:13:53', '2025-04-23 16:57:07', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (220, 220, '2025-04-23', 'Miércoles', '1:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-22 16:53:29', '2025-04-23 12:26:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (221, 221, '2025-04-23', 'Miércoles', '1:30 PM', '9.00', '9.00', '8.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-22 16:54:24', '2025-04-23 13:15:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (222, 222, '2025-04-23', 'Miércoles', '2:00 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-22 16:55:07', '2025-04-23 13:27:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (223, 223, '2025-04-23', 'Miércoles', '11:30 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-23 09:55:55', '2025-04-23 12:34:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (224, 224, '2025-04-23', 'Miércoles', '12:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-23 09:56:30', '2025-04-23 12:34:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (225, 225, '2025-04-23', 'Miércoles', '1:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-23 09:57:08', '2025-04-23 12:47:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (226, 226, '2025-04-23', 'Miércoles', '2:00 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-23 09:57:41', '2025-04-23 13:44:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (227, 227, '2025-04-23', 'Miércoles', '2:30 PM', '8.00', '8.00', '7.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-23 09:58:19', '2025-04-23 14:07:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (229, 229, '2025-04-24', 'Jueves', '7:30 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', '+ ANTISALITRE 6\"', 2, '2025-04-23 10:05:18', '2025-04-24 08:06:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (231, 231, '2025-04-24', 'Jueves', '1:30 PM', '14.00', '14.00', '13.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-23 17:32:59', '2025-04-24 13:44:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (232, 232, '2025-04-24', 'Jueves', '2:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-23 17:33:47', '2025-04-24 13:58:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (233, 233, '2025-04-24', 'Jueves', '1:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-23 17:34:56', '2025-04-24 12:16:19', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (234, 234, '2025-04-24', 'Jueves', '12:00 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-24 09:34:23', '2025-04-24 11:44:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (235, 235, '2025-04-24', 'Jueves', '1:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-24 09:34:58', '2025-04-24 12:42:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (236, 236, '2025-04-24', 'Jueves', '2:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-24 09:35:42', '2025-04-24 13:23:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (237, 237, '2025-04-24', 'Jueves', '2:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-24 09:36:43', '2025-04-24 15:53:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (239, 239, '2025-04-24', 'Jueves', '4:30 PM', '14.00', '14.00', '13.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1', 2, '2025-04-24 15:24:08', '2025-04-24 17:04:16', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (240, 240, '2025-04-25', 'Viernes', '1:00 PM', '5.00', '5.00', '4.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-24 17:52:26', '2025-04-25 12:25:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (241, 241, '2025-04-25', 'Viernes', '1:30 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-24 17:53:34', '2025-04-25 12:49:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (242, 242, '2025-04-25', 'Viernes', '2:30 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-24 17:54:44', '2025-04-25 13:40:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (243, 243, '2025-04-25', 'Viernes', '4:30 PM', '6.5', '6.5', '6.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA-LIMA-LURIGANCHO', 'SLUMP 8', 2, '2025-04-25 11:07:40', '2025-04-25 16:44:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (244, 244, '2025-04-25', 'Viernes', '1:00 PM', '7.00', '7.00', '6.75', '0', 'CAJAMARQUILLA', 'EN GUIA PASARLO COMO FC 210 THS H67 SLUMP 4-6\"', 2, '2025-04-25 11:37:11', '2025-04-29 10:48:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (245, 245, '2025-04-25', 'Viernes', '3:00 PM', '11.00', '11.00', '10.25', '0', 'HUAYCAN - ZONA R', NULL, 5, '2025-04-25 11:39:52', '2025-04-25 15:14:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (246, 246, '2025-04-26', 'Sábado', '8:30 AM', NULL, NULL, '1', '0', 'LA MOLINA', NULL, 6, '2025-04-26 08:26:58', '2025-04-26 12:15:37', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (247, 247, '2025-04-26', 'Sábado', '12:00 PM', NULL, NULL, '1', '0', 'CAJAMARQUILLA', NULL, 6, '2025-04-26 08:27:37', '2025-04-26 12:15:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (248, 248, '2025-04-26', 'Sábado', '3:00 PM', '17.5', '17.5', '16.5', '0', 'MEDIA LUNA', NULL, 6, '2025-04-26 08:39:35', '2025-04-26 16:30:49', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (249, 249, '2025-04-26', 'Sábado', '10:30 AM', '19.5', '18.00', '17.75', '0', 'SANTA CRUZ', NULL, NULL, '2025-04-26 08:50:05', '2025-04-26 12:18:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (250, 250, '2025-04-28', 'Lunes', '2:00 PM', '16.00', '16.00', '15.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-04-26 09:32:30', '2025-04-29 12:30:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (251, 251, '2025-04-28', 'Lunes', '1:00 PM', '5.5', '5.5', '5.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-26 11:43:55', '2025-04-28 12:17:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (252, 252, '2025-04-28', 'Lunes', '1:30 PM', '9.5', '9.5', '9', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-26 11:44:40', '2025-04-28 13:14:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (253, 253, '2025-04-28', 'Lunes', '2:30 PM', '4.5', '4.5', '4.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-26 11:45:23', '2025-04-28 14:11:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (254, 254, '2025-04-28', 'Lunes', '1:30 PM', '8.00', '8.00', '7.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-28 09:15:56', '2025-04-28 12:40:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (255, 255, '2025-04-28', 'Lunes', '2:30 PM', '11.00', '11.00', '10.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-28 09:16:33', '2025-04-28 13:58:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (256, 256, '2025-04-28', 'Lunes', '3:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-28 09:17:06', '2025-04-28 14:51:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (257, 257, '2025-04-28', 'Lunes', '3:30 PM', '6.5', '6.5', '6.25', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-28 09:17:47', '2025-04-28 16:36:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (259, 259, '2025-04-28', 'Lunes', '5:30 PM', '5.00', '5.00', '4.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', 2, '2025-04-28 17:34:57', '2025-04-28 17:59:43', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (260, 260, '2025-04-29', 'Martes', '1:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-28 17:35:05', '2025-04-29 12:47:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (261, 261, '2025-04-29', 'Martes', '1:30 PM', '11.00', '11.00', '10.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-28 17:36:13', '2025-04-29 13:30:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (262, 262, '2025-04-29', 'Martes', '2:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-28 17:37:14', '2025-04-29 14:38:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (263, 263, '2025-04-29', 'Martes', '2:00 PM', '14.00', '14.00', '13.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-04-28 17:41:42', '2025-05-21 13:45:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (264, 264, '2025-04-29', 'Martes', '7:30 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-04-28 17:57:27', '2025-04-29 07:45:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (265, 265, '2025-04-29', 'Martes', '8:00 AM', '6.00', '6.00', '5.75', '0', 'JR INCA RIPAC 326', 'SLUMP 4-6', 2, '2025-04-28 18:04:16', '2025-04-29 07:09:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (266, 266, '2025-04-29', 'Martes', '8:00 AM', '12.00', '12.00', '11.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', 2, '2025-04-29 07:58:34', '2025-04-29 08:25:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (267, 267, '2025-04-29', 'Martes', '9:00 AM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'SLUMP 6', 6, '2025-04-29 08:17:40', '2025-04-29 09:14:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (268, 268, '2025-04-29', 'Martes', '2:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-29 08:29:25', '2025-04-29 14:21:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (269, 269, '2025-04-29', 'Martes', '1:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-29 08:30:21', '2025-04-29 13:08:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (270, 270, '2025-04-29', 'Martes', '12:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-04-29 08:30:55', '2025-04-29 12:21:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (273, 273, '2025-04-29', 'Martes', '3:00 PM', '8.00', '8.00', '7.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', NULL, '2025-04-29 15:42:24', '2025-04-29 16:15:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (275, 275, '2025-04-30', 'Miércoles', '9:00 AM', '7.00', '7.00', '6.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', 'SLUMP 6', 6, '2025-04-29 17:11:31', '2025-04-30 11:59:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (276, 276, '2025-04-30', 'Miércoles', '1:45 PM', '8.00', '8.00', '8', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-29 17:12:13', '2025-04-30 13:21:49', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (277, 277, '2025-04-30', 'Miércoles', '2:30 PM', '8.00', '8.00', '7.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-29 17:13:22', '2025-04-30 14:27:16', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (278, 278, '2025-04-30', 'Miércoles', '8:00 AM', '10.50', '9.50', '9.5', '0', 'HUACHIPA NORTE', 'SLUMP 4', 4, '2025-04-30 08:34:26', '2025-04-30 09:04:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (279, 279, '2025-04-30', 'Miércoles', '8:30 AM', '10.00', '9.00', '8.75', '0', 'CAJAMARQUILLA', NULL, 2, '2025-04-30 08:40:46', '2025-04-30 09:46:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (281, 281, '2025-04-30', 'Miércoles', '1:00 PM', '17.50', '17.50', '17', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-04-30 08:57:45', '2025-04-30 14:07:03', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (283, 283, '2025-04-30', 'Miércoles', '3:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-04-30 08:57:46', '2025-04-30 15:07:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (285, 285, '2025-04-30', 'Miércoles', '2:00 PM', '8.00', '8.00', '5.75', '-1.75', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-04-30 10:45:03', '2025-04-30 17:05:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (286, 286, '2025-04-30', 'Miércoles', '3:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-04-30 11:25:13', '2025-04-30 14:46:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (287, 287, '2025-04-30', 'Miércoles', '1:00 PM', '8.00', '8.00', '7.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-04-30 11:28:52', '2025-04-30 12:35:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (289, 289, '2025-04-30', 'Miércoles', '2:00 PM', '8.00', '8.00', '7.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 6, '2025-04-30 11:35:17', '2025-06-30 09:09:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (290, 290, '2025-04-30', 'Miércoles', '6:00 PM', '6.00', '6.00', '5.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', NULL, '2025-04-30 18:14:37', '2025-04-30 18:35:27', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (291, 291, '2025-05-02', 'Viernes', '9:30 AM', '12.00', '12.00', '11.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-02 08:28:27', '2025-05-02 09:31:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (292, 292, '2025-05-02', 'Viernes', '10:00 AM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-02 08:31:11', '2025-05-02 10:21:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (293, 293, '2025-05-02', 'Viernes', '11:00 AM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-02 08:32:08', '2025-05-02 10:48:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (294, 294, '2025-05-02', 'Viernes', '12:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-02 08:32:49', '2025-05-02 11:33:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (295, 295, '2025-05-02', 'Viernes', '2:30 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-02 08:33:26', '2025-05-02 14:43:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (296, 296, '2025-05-02', 'Viernes', '8:30 AM', '7.00', '7.00', '6.75', '0', 'CAJAMARQUILLA', 'SLUMP 6', NULL, '2025-05-02 09:13:01', '2025-05-02 09:52:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (297, 297, '2025-05-02', 'Viernes', '1:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP', 4, '2025-05-02 09:18:53', '2025-05-02 12:14:14', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (298, 298, '2025-05-02', 'Viernes', '1:30 PM', '12.00', '12.00', '11.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-02 09:19:53', '2025-05-02 12:46:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (299, 299, '2025-05-02', 'Viernes', '2:30 PM', '8.5', '8.5', '8.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-02 09:20:42', '2025-05-02 13:55:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (300, 300, '2025-05-02', 'Viernes', '2:30 PM', '14.00', '14.00', '13.75', '0.25', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-05-02 09:25:17', '2025-05-02 14:18:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (302, 302, '2025-05-05', 'Lunes', '12:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 08:42:04', '2025-05-05 11:59:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (303, 303, '2025-05-05', 'Lunes', '12:30 PM', '12.00', '12.00', '11.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 08:43:34', '2025-05-05 12:20:58', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (304, 304, '2025-05-05', 'Lunes', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 08:45:58', '2025-05-05 13:34:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (306, 306, '2025-05-05', 'Lunes', '2:00 PM', '20.00', '20.00', '22', '3', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-05-05 08:57:55', '2025-05-05 18:32:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (307, 307, '2025-05-05', 'Lunes', '2:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-05 08:59:23', '2025-05-05 13:54:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (308, 308, '2025-05-05', 'Lunes', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-05 09:00:14', '2025-05-05 12:43:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (309, 309, '2025-05-05', 'Lunes', '3:00 PM', '7.00', '7.00', '6.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-05 09:01:21', '2025-05-05 14:13:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (310, 310, '2025-05-05', 'Lunes', '3:00 PM', '8.00', '8.00', '0', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-05 10:22:52', '2025-05-05 15:41:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (312, 312, '2025-05-06', 'Martes', '2:00 PM', '23.00', '23.00', '22.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-05 18:27:42', '2025-05-06 14:40:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (314, 314, '2025-05-06', 'Martes', '9:30 AM', '24.00', '24.00', '23.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 18:34:53', '2025-05-06 09:39:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (315, 315, '2025-05-06', 'Martes', '12:30 PM', '12.00', '12.00', '11.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 18:35:59', '2025-05-06 12:24:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (316, 316, '2025-05-06', 'Martes', '2:30 PM', '12.00', '12.00', '11.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-05 18:36:42', '2025-05-06 14:14:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (317, 317, '2025-05-06', 'Martes', '1:30 PM', '9.00', '9.00', '8.75', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', NULL, 4, '2025-05-06 12:18:39', '2025-05-06 12:43:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (318, 318, '2025-05-06', 'Martes', '2:00 PM', '9.50', '9.50', '9.25', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 6', 4, '2025-05-06 12:53:54', '2025-05-06 13:34:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (319, 319, '2025-05-06', 'Martes', '2:30 PM', '7.50', '7.50', '7.25', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 8', 4, '2025-05-06 13:32:26', '2025-05-06 13:52:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (320, 320, '2025-05-07', 'Miércoles', '8:00 AM', '9.00', '9.00', '8.5', '0', 'JR. INCA RIPAC 326', NULL, 2, '2025-05-06 18:35:57', '2025-05-07 07:27:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (321, 321, '2025-05-07', 'Miércoles', '2:00 PM', '13.00', '13.00', '22', '9.5', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-06 18:38:56', '2025-05-07 13:48:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (322, 322, '2025-05-07', 'Miércoles', '11:30 AM', '10', '10', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-06 18:43:13', '2025-05-07 11:23:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (323, 323, '2025-05-07', 'Miércoles', '12:30 PM', '12', '12', '11.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-06 18:46:55', '2025-05-07 11:44:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (324, 324, '2025-05-07', 'Miércoles', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-06 18:48:36', '2025-05-07 18:22:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (325, 325, '2025-05-07', 'Miércoles', '8:00 AM', '6.00', '6.00', '5.5', '0', 'JIRÓN HUÁSCAR 2170 OYAGUE - JESUS MARIA.', NULL, 2, '2025-05-06 18:52:07', '2025-05-07 07:48:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (326, 326, '2025-05-07', 'Miércoles', '1:30 PM', '8.00', '8.00', '7.5', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 6', 4, '2025-05-06 19:01:59', '2025-05-07 12:45:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (327, 327, '2025-05-07', 'Miércoles', '2:00 PM', '10.00', '10.00', '10', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 6', 4, '2025-05-06 19:03:23', '2025-05-07 14:01:13', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (328, 328, '2025-05-07', 'Miércoles', '3:00 PM', '8.00', '8.00', '7.5', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 8', 4, '2025-05-06 19:04:26', '2025-05-07 14:18:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (329, 329, '2025-05-08', 'Jueves', '7:30 AM', '10.00', '10.00', '9.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-07 12:34:13', '2025-05-08 08:04:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (330, 330, '2025-05-08', 'Jueves', '8:00 AM', '6.00', '6.00', '5.5', '0', 'JIRÓN HUÁSCAR 2170 OYAGUE - JESUS MARIA.', NULL, 2, '2025-05-07 16:42:14', '2025-05-08 07:03:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (331, 331, '2025-05-08', 'Jueves', '10:30 AM', '10.00', '10.00', '9.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-05-07 16:44:31', '2025-05-08 10:47:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (332, 332, '2025-05-08', 'Jueves', '2:30 PM', '8.00', '8.00', '7.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-05-07 17:24:44', '2025-05-08 14:54:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (333, 333, '2025-05-08', 'Jueves', '1:30 PM', '7.50', '7.50', '7.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-07 17:42:45', '2025-05-08 12:30:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (334, 334, '2025-05-08', 'Jueves', '2:00 PM', '16.50', '16.50', '16', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-07 17:43:48', '2025-05-08 13:47:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (335, 335, '2025-05-08', 'Jueves', '3:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-07 17:44:44', '2025-05-08 14:16:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (337, 337, '2025-05-08', 'Jueves', '11:30 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-07 18:24:35', '2025-05-08 11:14:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (338, 338, '2025-05-08', 'Jueves', '12:30 PM', '13.00', '13.00', '12.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-07 18:25:53', '2025-05-08 11:55:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (339, 339, '2025-05-08', 'Jueves', '1:30 PM', '13.00', '13.00', '12.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-07 18:27:12', '2025-05-08 12:59:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (341, 341, '2025-05-09', 'Viernes', '11:30 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-09 09:13:16', '2025-05-09 11:22:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (342, 342, '2025-05-09', 'Viernes', '12:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-09 09:14:08', '2025-05-09 11:50:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (343, 343, '2025-05-09', 'Viernes', '1:30 PM', '13.00', '13.00', '12.5', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-09 09:15:35', '2025-05-09 12:50:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (344, 344, '2025-05-09', 'Viernes', '2:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-09 09:18:47', '2025-05-09 13:39:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (345, 345, '2025-05-09', 'Viernes', '2:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-09 09:37:12', '2025-05-09 13:04:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (346, 346, '2025-05-09', 'Viernes', '3:00 PM', '7.5', '7.5', '7.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 PROYECTO PV1 - CAMPO FE', 2, '2025-05-09 12:01:09', '2025-05-09 15:39:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (347, 347, '2025-05-07', 'Miércoles', '1:00 PM', '27.00', '27.00', '27.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', 'BOMBEO UNICON', 4, '2025-05-09 12:34:57', '2025-09-09 16:59:58', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (348, 348, '2025-05-08', 'Jueves', '1:00 PM', '5.00', '5.00', '5.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', 'BOMBEO UNICON', 4, '2025-05-09 12:36:18', '2025-09-09 16:59:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (349, 349, '2025-05-10', 'Sábado', '3:00 PM', '8.00', '8.00', '7.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA LIMA -LIMA -LURIGANCHO', 'SLUMP 8 NAVE J1', 2, '2025-05-10 08:40:14', '2025-05-10 14:35:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (350, 350, '2025-05-12', 'Lunes', '1:00 PM', '16.00', '16.00', '15.75', '0', 'JR. INCA RIPAC 326', NULL, 2, '2025-05-12 08:36:14', '2025-05-12 12:57:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (351, 351, '2025-05-12', 'Lunes', '2:00 PM', '12.00', '12.00', '11.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-12 08:46:11', '2025-05-12 13:41:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (353, 353, '2025-05-12', 'Lunes', '3:00 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-12 08:49:05', '2025-05-12 14:00:03', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (357, 357, '2025-05-13', 'Martes', '3:00 PM', '5.00', '5.00', '4.75', '0', 'JR. INCA RIPAC 326', NULL, 2, '2025-05-12 17:35:23', '2025-05-13 14:08:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (358, 358, '2025-05-13', 'Martes', '2:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 2, '2025-05-12 17:37:19', '2025-05-13 13:27:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (359, 359, '2025-05-14', 'Miércoles', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-12 17:41:39', '2025-05-14 12:43:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (360, 360, '2025-05-14', 'Miércoles', '2:00 PM', '19.00', '19.00', '18.5', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 4, '2025-05-12 17:43:10', '2025-05-14 13:32:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (361, 361, '2025-05-14', 'Miércoles', '2:30 PM', '6.5', '6.5', '6.25', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-12 17:44:21', '2025-05-14 13:53:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (362, 362, '2025-05-13', 'Martes', '8:00 AM', '10.00', '10.00', '9.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', 'SLUMP 6', 2, '2025-05-13 08:27:23', '2025-05-13 08:46:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (363, 363, '2025-05-13', 'Martes', '4:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'SLUMP 6', 4, '2025-05-13 14:47:50', '2025-05-13 15:21:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (364, 364, '2025-05-13', 'Martes', '4:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'SLUMP 6', 4, '2025-05-13 14:49:04', '2025-05-13 15:43:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (365, 365, '2025-05-13', 'Martes', '4:00 PM', '23.00', '23.00', '22.5', '0', 'NIEVERIA', NULL, 2, '2025-05-13 15:07:28', '2025-05-13 17:58:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (367, 367, '2025-05-14', 'Miércoles', '10:00 AM', '6.50', '6.50', '6.25', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-13 16:23:27', '2025-05-14 07:56:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (368, 368, '2025-05-14', 'Miércoles', '8:00 AM', '24.00', '24.00', '24.00', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA JESUS MARIA', 'BOMBEO UNICON', 7, '2025-05-14 09:43:40', '2025-09-09 17:01:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (369, 369, '2025-05-15', 'Jueves', '10:00 AM', '6.00', '6.00', '5.74', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-14 12:41:58', '2025-05-15 07:52:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (370, 370, '2025-05-15', 'Jueves', '6:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-14 15:49:25', '2025-05-15 07:32:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (371, 371, '2025-05-15', 'Jueves', '8:30 AM', '44.50', '44.50', '44.50', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA JESUS MARIA', 'BOMBEO UNICON', 6, '2025-05-15 08:40:34', '2025-09-09 17:01:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (372, 372, '2025-05-15', 'Jueves', '12:00 PM', '12.00', '12.00', '11.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-15 11:27:22', '2025-05-15 13:24:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (373, 373, '2025-05-17', 'Sábado', '12:00 PM', '20.00', '18.00', '17.75', '0', 'NIEVERIA', NULL, 7, '2025-05-15 12:33:37', '2025-05-17 13:04:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (374, 374, '2025-05-16', 'Viernes', '10:00 AM', '5.00', '5.00', '4.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-15 12:59:21', '2025-05-16 08:04:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (375, 375, '2025-05-16', 'Viernes', '2:00 PM', '16.5', '16.5', '17', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMO 6-8', 2, '2025-05-15 17:21:53', '2025-05-19 10:46:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (376, 376, '2025-05-16', 'Viernes', '1:30 PM', '13.00', '13.00', '12.5', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-15 18:05:04', '2025-05-16 12:34:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (377, 377, '2025-05-16', 'Viernes', '2:30 PM', '7.5', '7.5', '7.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-15 18:05:46', '2025-05-16 13:55:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (378, 378, '2025-05-16', 'Viernes', '2:00 PM', '13.00', '13.00', '12.5', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-15 18:07:26', '2025-05-16 13:09:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (379, 379, '2025-05-16', 'Viernes', '10:00 AM', '20.00', '19.00', '18.5', '0', 'NIEVERIA', 'SLUMP 6-8', 6, '2025-05-15 18:16:03', '2025-05-19 12:23:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (380, 380, '2025-05-16', 'Viernes', '8:00 AM', '63.00', '63.00', '63.00', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA JESUS MARIA', 'BOMBEO UNICON 50M3 + 14M3', 6, '2025-05-16 09:06:47', '2025-09-09 17:00:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (381, 381, '2025-05-17', 'Sábado', '7:30 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-16 09:13:03', '2025-05-17 08:09:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (382, 382, '2025-05-17', 'Sábado', '8:00 AM', '12.00', '11.00', '10.75', '0', 'CAMACHO - LA MOLINA', 'NO ES PISO PULIDO, ES PARA MAYOLICA', 7, '2025-05-16 10:51:19', '2025-05-17 08:36:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (384, 384, '2025-05-16', 'Viernes', '3:00 PM', '6.5', '6.5', '6.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-16 12:14:09', '2025-05-16 14:32:13', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (385, 385, '2025-05-19', 'Lunes', '2:30 PM', '6.50', '6.50', '6.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 4, '2025-05-17 10:48:09', '2025-05-19 13:27:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (386, 386, '2025-05-09', 'Viernes', '10:00 AM', '7.00', '7.00', '7.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'BOMBEO UNICON', 7, '2025-05-17 10:56:37', '2025-09-09 17:02:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (387, 387, '2025-05-13', 'Martes', '1:00 PM', '7.00', '7.00', '7.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 7, '2025-05-17 11:02:51', '2025-09-09 17:01:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (388, 388, '2025-05-12', 'Lunes', '10:00 AM', '42.00', '42.00', '42.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', 'BOMBEO UNICON', 6, '2025-05-17 11:05:55', '2025-09-09 17:02:16', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (390, 390, '2025-05-19', 'Lunes', '11:00 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-17 11:16:58', '2025-05-19 10:40:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (391, 391, '2025-05-19', 'Lunes', '12:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-17 11:21:38', '2025-05-19 11:41:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (392, 392, '2025-05-19', 'Lunes', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-17 11:22:18', '2025-05-19 12:06:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (393, 393, '2025-05-19', 'Lunes', '1:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-17 11:23:07', '2025-05-19 12:48:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (394, 394, '2025-05-19', 'Lunes', '10:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-17 12:15:12', '2025-05-19 07:28:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (395, 395, '2025-05-19', 'Lunes', '2:00 PM', '17.00', '17.00', '16.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 2, '2025-05-17 12:45:53', '2025-05-19 13:51:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (396, 396, '2025-05-19', 'Lunes', '10:00 AM', '7.00', '7.00', '7.00', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', 'BOMBEO UNICON', 6, '2025-05-19 09:28:36', '2025-09-09 17:00:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (397, 397, '2025-05-19', 'Lunes', '4:00 PM', '5.5', '5.5', '5.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA-LIMA-LURIGANCHO', 'SLUMP 8 PV1', 2, '2025-05-19 12:47:58', '2025-05-19 16:48:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (398, 398, '2025-05-20', 'Martes', '1:30 PM', '14.00', '14.00', '13.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-19 17:11:07', '2025-05-20 14:16:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (399, 399, '2025-05-20', 'Martes', '12:00 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-20 08:32:39', '2025-05-20 11:41:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (400, 400, '2025-05-20', 'Martes', '12:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-20 08:35:30', '2025-05-20 12:25:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (401, 401, '2025-05-20', 'Martes', '1:00 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-20 08:37:48', '2025-05-20 12:58:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (402, 402, '2025-05-20', 'Martes', '2:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-20 08:38:35', '2025-05-20 13:58:07', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (403, 403, '2025-05-20', 'Martes', '4:00 PM', '5.00', '5.00', '4.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 PV1', 2, '2025-05-20 08:40:46', '2025-05-20 16:21:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (404, 404, '2025-05-21', 'Miércoles', '1:30 PM', '13.00', '13.00', '12.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-20 15:01:45', '2025-05-21 13:02:14', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (405, 405, '2025-05-21', 'Miércoles', '2:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-21 08:50:16', '2025-05-21 13:21:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (406, 406, '2025-05-21', 'Miércoles', '2:30 PM', '13.00', '13.00', '12.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-21 08:51:28', '2025-05-21 13:46:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (407, 407, '2025-05-21', 'Miércoles', '7:30 AM', '6.50', '6.50', '6.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-21 08:52:26', '2025-05-21 14:13:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (408, 408, '2025-05-22', 'Jueves', '7:00 AM', '7.00', '7.00', '6.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-21 08:53:32', '2025-05-22 08:02:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (409, 409, '2025-05-22', 'Jueves', '3:00 PM', '5.00', '5.00', '4.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 + FIBRA METALICA NAVE G3', 2, '2025-05-21 09:11:39', '2025-05-22 15:50:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (410, 410, '2025-05-22', 'Jueves', '3:00 PM', '10.00', '10.00', '9.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 2, '2025-05-21 09:13:25', '2025-05-22 14:46:47', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (411, 411, '2025-05-22', 'Jueves', '8:00 AM', '6.5', '6.5', '6.25', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-21 09:15:54', '2025-05-22 08:18:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (412, 412, '2025-05-22', 'Jueves', '10:00 AM', '8.00', '8.00', '7.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-21 16:58:35', '2025-05-22 08:48:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (413, 413, '2025-05-22', 'Jueves', '10:30 AM', '7.50', '7.50', '7.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-21 16:59:36', '2025-05-22 09:07:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (414, 414, '2025-05-22', 'Jueves', '11:00 AM', '6.50', '6.50', '6.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 4, '2025-05-21 17:01:31', '2025-05-22 09:40:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (415, 415, '2025-05-22', 'Jueves', '2:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-22 08:40:53', '2025-05-22 13:25:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (416, 416, '2025-05-22', 'Jueves', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-22 08:41:08', '2025-05-22 12:05:32', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (417, 417, '2025-05-22', 'Jueves', '1:30 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-22 08:42:23', '2025-05-22 13:00:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (418, 418, '2025-05-22', 'Jueves', '2:30 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-22 08:44:09', '2025-05-22 14:01:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (419, 419, '2025-05-22', 'Jueves', '3:00 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-05-22 08:45:24', '2025-05-22 14:25:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (420, 420, '2025-05-21', 'Miércoles', '10:00 AM', '14.00', '14.00', '14.00', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', 'BOMBEO UNICON', 6, '2025-05-22 09:45:56', '2025-09-09 16:59:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (421, 421, '2025-05-23', 'Viernes', '10:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-23 08:40:16', '2025-05-23 10:16:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (422, 422, '2025-05-23', 'Viernes', '8:00 AM', '14.00', '14.00', '14.00', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', 'BOMBEO UNICON', 6, '2025-05-23 08:44:40', '2025-09-09 16:58:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (423, 423, '2025-05-23', 'Viernes', '3:30 PM', '25.00', '25.00', '24.50', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 2, '2025-05-23 08:46:30', '2025-05-23 15:21:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (424, 424, '2025-05-23', 'Viernes', '12:00 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA-LIMA-JESUS MARIA', NULL, 6, '2025-05-23 08:48:42', '2025-05-23 12:27:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (425, 425, '2025-05-23', 'Viernes', '10:30 AM', '5.00', '5.00', '4.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA-LIMA-LURIGANCHO', NULL, 2, '2025-05-23 11:11:00', '2025-05-23 11:37:45', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (426, 426, '2025-05-23', 'Viernes', '1:00 PM', '1.50', '1.50', '1.50', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', NULL, NULL, '2025-05-23 13:45:12', '2025-05-23 14:07:52', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (427, 427, '2025-05-24', 'Sábado', '10:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-23 16:55:20', '2025-05-24 07:55:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (429, 429, '2025-05-26', 'Lunes', '1:30 PM', '6.5', '6.5', '6.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 5', 4, '2025-05-24 09:34:17', '2025-05-26 12:37:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (430, 430, '2025-05-26', 'Lunes', '2:00 PM', '11.50', '11.50', '11', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 4, '2025-05-24 09:35:56', '2025-05-26 13:20:26', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (431, 431, '2025-05-26', 'Lunes', '2:30 PM', '8.00', '8.00', '7.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 4, '2025-05-24 09:36:50', '2025-05-26 13:42:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (432, 432, '2025-05-26', 'Lunes', '11:00 AM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-26 09:40:14', '2025-05-26 10:44:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (433, 433, '2025-05-26', 'Lunes', '12:00 AM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-26 09:44:56', '2025-05-26 11:14:25', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (434, 434, '2025-05-26', 'Lunes', '1:00 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-26 09:46:02', '2025-05-26 12:06:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (435, 435, '2025-05-26', 'Lunes', '1:30 PM', '6.00', '6.00', '5.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-26 09:46:45', '2025-05-26 12:56:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (436, 436, '2025-05-27', 'Martes', '10:00 AM', '6.5', '6.50', '6.25', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-26 17:28:20', '2025-05-27 08:56:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (437, 437, '2025-05-27', 'Martes', '10:00 AM', '7.00', '7.00', '6.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-27 08:27:58', '2025-05-27 10:21:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (438, 438, '2025-05-27', 'Martes', '12:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-27 09:07:53', '2025-06-03 11:31:45', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (439, 439, '2025-05-27', 'Martes', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-27 09:08:47', '2025-06-03 11:31:39', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (442, 442, '2025-05-27', 'Martes', '2:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-27 09:11:34', '2025-06-03 11:31:31', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (446, 446, '2025-05-28', 'Miércoles', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-28 08:43:30', '2025-05-28 12:04:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (447, 447, '2025-05-28', 'Miércoles', '1:00 PM', '11.00', '11.00', '10.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-28 08:45:03', '2025-05-28 12:38:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (448, 448, '2025-05-28', 'Miércoles', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-28 08:47:08', '2025-05-28 13:05:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (449, 449, '2025-05-28', 'Miércoles', '2:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-28 08:47:51', '2025-05-28 13:29:09', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (450, 450, '2025-05-28', 'Miércoles', '2:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-28 08:49:23', '2025-05-28 13:54:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (451, 451, '2025-05-29', 'Jueves', '8:00 AM', '12.00', '12.00', '11.75', '0', 'EL CONDOMINIO DE ENSUEÑO, LIMA - HUAROCHIRI - RICARDO PALMA', 'SLUMP 4-6', 2, '2025-05-28 11:30:56', '2025-05-29 07:46:18', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (452, 452, '2025-05-30', 'Viernes', '8:00 AM', '22.00', '22.00', '21.5', '0', 'EL CONDOMINIO DE ENSUEÑO, LIMA - HUAROCHIRI - RICARDO PALMA', 'FRECUENCIA DE MIXER A 1.5 HORAS SLUMP 4-6', 2, '2025-05-28 16:17:45', '2025-05-30 09:26:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (453, 453, '2025-05-29', 'Jueves', '2:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8 EN OBRA - SE ATENDIO 3.5 M3', 4, '2025-05-28 16:42:03', '2025-06-02 18:00:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (454, 454, '2025-05-29', 'Jueves', '2:30 PM', '6.00', '6.00', '5.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-28 16:43:14', '2025-05-29 14:04:10', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (455, 455, '2025-05-29', 'Jueves', '2:00 PM', '7.00', '7.00', '6.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-28 17:49:14', '2025-05-29 12:48:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (456, 456, '2025-05-29', 'Jueves', '8:00 AM', '6.00', '5.00', '4.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', 'SLUMP 4-6', NULL, '2025-05-29 06:33:19', '2025-05-30 12:34:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (457, 457, '2025-05-29', 'Jueves', '1:00 PM', '16', '16', '19.5', '4', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-05-29 08:48:42', '2025-05-29 13:09:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (458, 458, '2025-05-31', 'Sábado', '8:00 AM', '5.00', '5.00', '4.5', '0', 'HUACHIPA', NULL, 6, '2025-05-29 09:32:44', '2025-05-31 09:42:46', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (459, 459, '2025-05-29', 'Jueves', '3:00 PM', '7.00', '7.00', '6.75', '0', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 8', 4, '2025-05-29 10:06:03', '2025-05-29 14:24:13', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (460, 460, '2025-05-29', 'Jueves', '10:00 AM', '8.00', '8.00', '0', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, NULL, '2025-05-29 10:49:06', '2025-05-29 10:49:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (461, 461, '2025-05-29', 'Jueves', '1:00 PM', '8.00', '8.00', '7.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-05-29 10:51:48', '2025-05-29 13:49:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (462, 462, '2025-05-30', 'Viernes', '8:00 AM', '8.00', '8.00', '7.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-29 17:16:23', '2025-05-30 06:55:34', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (463, 463, '2025-05-30', 'Viernes', '12:00 PM', '7.00', '7.00', '6.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-30 09:22:54', '2025-05-30 11:42:40', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (464, 464, '2025-05-30', 'Viernes', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-30 09:24:30', '2025-05-30 12:09:38', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (465, 465, '2025-05-30', 'Viernes', '1:00 PM', '7.00', '7.00', '6.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-30 09:27:16', '2025-05-30 12:27:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (466, 466, '2025-05-30', 'Viernes', '1:30 PM', '7.00', '7.00', '6.75', '0', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-30 09:28:44', '2025-05-30 12:41:45', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (467, 467, '2025-05-30', 'Viernes', '2:00 PM', '6.00', '5.00', '4.75', '-4.75', 'AV GRAL SANTA CRUZ 708 ESQUINA CON JIRON PUMACAHUA LIMA LIMA - JESUS MARIA', NULL, 6, '2025-05-30 09:29:38', '2025-05-30 13:34:11', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (468, 468, '2025-05-30', 'Viernes', '4:00 PM', '6.00', '4.00', '3.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA- LIMA-LURIGANCHO', NULL, 2, '2025-05-30 14:51:21', '2025-05-30 16:19:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (469, 469, '2025-05-31', 'Sábado', '8:00 AM', '8.00', '8.00', '7.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-05-30 14:59:21', '2025-05-31 06:59:33', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (470, 470, '2025-05-31', 'Sábado', '11:00 AM', '7.00', '7.00', '6.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA- LIMA-LURIGANCHO', NULL, 2, '2025-05-30 15:32:18', '2025-05-31 11:31:07', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (471, 471, '2025-06-02', 'Lunes', '2:00 PM', '17.00', '17.00', '16.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 2, '2025-06-02 08:47:17', '2025-06-02 13:33:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (472, 472, '2025-06-02', 'Lunes', '11:00 AM', '10.00', '10.00', '9.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-06-02 09:16:55', '2025-06-02 09:47:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (473, 473, '2025-06-03', 'Martes', '2:00 PM', '7.50', '7.50', '7.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 2, '2025-06-02 14:24:50', '2025-06-03 14:04:17', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (474, 474, '2025-06-03', 'Martes', '3:00 PM', '6.00', '3.00', '2.75', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'NAVE G7', 2, '2025-06-03 08:42:56', '2025-06-03 15:58:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (475, 475, '2025-06-03', 'Martes', '2:00 PM', '13.5', '13.5', '13.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 2, '2025-06-03 09:00:46', '2025-06-03 13:40:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (476, 476, '2025-06-04', 'Miércoles', '10:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-06-03 12:38:46', '2025-06-04 10:40:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (477, 477, '2025-06-04', 'Miércoles', '10:00 AM', '5.50', '5.50', '5.25', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-06-03 17:14:26', '2025-06-04 08:13:13', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (478, 478, '2025-06-04', 'Miércoles', '9:30 AM', '9.50', '9.50', '9.25', '0', 'AV. GIUSSEPE GARIBALDI 217 - JESUS MARIA', 'SLUMP 8 EN OBRA', 4, '2025-06-03 17:35:32', '2025-06-04 08:33:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (479, 479, '2025-06-04', 'Miércoles', '12:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-06-04 08:28:43', '2025-06-04 12:28:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (480, 480, '2025-06-04', 'Miércoles', '1:00 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-06-04 08:30:03', '2025-06-04 12:53:15', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (481, 481, '2025-06-04', 'Miércoles', '1:30 PM', '10.00', '10.00', '9.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-06-04 08:32:18', '2025-06-04 13:51:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (482, 482, '2025-06-04', 'Miércoles', '2:30 PM', '12.00', '12.00', '11.75', '0', 'AV. GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACACHA, LIMA- LIMA- JESUS MARIA', NULL, 6, '2025-06-04 08:39:16', '2025-06-04 14:37:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (483, 483, '2025-06-04', 'Miércoles', '2:00 PM', '12.50', '12.50', '12', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 2, '2025-06-04 10:54:47', '2025-06-04 14:10:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (484, 484, '2025-06-05', 'Jueves', '9:00 AM', '6.00', '5.50', '5.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 NAVE G7 - CON FIBRA METÁLICA CON ACELERANTE A 3 DÍAS', 2, '2025-06-04 14:29:57', '2025-06-05 09:42:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (485, 485, '2025-06-05', 'Jueves', '2:00 PM', '15.50', '15.50', '15', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 2, '2025-06-04 14:39:25', '2025-06-05 14:49:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (486, 486, '2025-06-05', 'Jueves', '1:00 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', 'SLUMP 4-6', 6, '2025-06-04 16:36:16', '2025-06-05 12:34:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (487, 487, '2025-06-05', 'Jueves', '1:30 PM', '12.00', '12.00', '11.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-04 16:43:07', '2025-06-05 13:44:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (488, 488, '2025-06-05', 'Jueves', '2:30 PM', '12.00', '12.00', '11.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-04 16:44:17', '2025-06-05 14:35:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (489, 489, '2025-06-05', 'Jueves', '1:00 PM', '6.00', '3.00', '3', '0', 'CAMPO FE - LURIN', NULL, 2, '2025-06-04 16:45:16', '2025-06-05 12:57:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (490, 490, '2025-06-05', 'Jueves', '2:30 PM', '7.50', '7.50', '7.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 2, '2025-06-04 16:46:36', '2025-06-05 13:27:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (491, 491, '2025-06-06', 'Viernes', '2:00 PM', '16.50', '16.50', '15.5', '-0.5', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 2, '2025-06-05 10:25:09', '2025-06-06 14:02:43', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (492, 492, '2025-06-06', 'Viernes', '10:00 AM', '6.00', '6.00', '5.75', '0', 'CAMPO FE HUACHIPA LIMA LIMA - LURIGANCHO', NULL, 2, '2025-06-05 15:13:25', '2025-06-06 10:39:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (493, 493, '2025-06-06', 'Viernes', '11:30 AM', '10.00', '10.00', '9.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-06 09:32:25', '2025-06-06 11:08:23', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (494, 494, '2025-06-06', 'Viernes', '1:00 PM', '13.00', '13.00', '12.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIAAV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-06 09:33:58', '2025-06-06 12:34:36', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (495, 495, '2025-06-06', 'Viernes', '1:30 PM', '13.00', '13.00', '12.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-06 09:35:00', '2025-06-06 12:57:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (496, 496, '2025-06-06', 'Viernes', '2:00 PM', '10.00', '10.00', '9.75', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-06 09:37:03', '2025-06-06 13:43:35', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (497, 497, '2025-06-06', 'Viernes', '2:30 PM', '8.00', '8.00', '7.5', '0', 'AV GRAL. SANTA CRUZ 708, ESQUINA CON JIRON PUMACAHUA, LIMA - LIMA- JESUS MARIA', NULL, 6, '2025-06-06 09:38:29', '2025-06-06 14:18:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (498, 498, '2025-06-09', 'Lunes', '2:00 PM', '25.00', '25.00', '24.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 4, '2025-06-09 08:40:39', '2025-06-09 13:57:03', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (499, 499, '2025-06-10', 'Martes', '8:30 AM', '22.00', '20.50', '20.25', '0', 'HUACHIPA', NULL, 2, '2025-06-10 08:41:19', '2025-06-10 10:11:02', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (501, 501, '2025-06-10', 'Martes', '2:00 PM', '5.00', '4.00', '4', '0', 'HUACHIPA', 'SLUMP 4-6', 2, '2025-06-10 08:44:20', '2025-06-10 14:41:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (502, 502, '2025-06-10', 'Martes', '2:00 PM', '11.00', '11.00', '10.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6', 6, '2025-06-10 08:48:22', '2025-06-10 13:28:30', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (503, 503, '2025-06-10', 'Martes', '2:30 PM', '7.50', '7.50', '7.25', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 6, '2025-06-10 08:50:07', '2025-06-10 16:35:43', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (504, 504, '2025-06-10', 'Martes', '10:00 AM', '8.50', '7.00', '6.75', '0', 'VALLE GRANDE ATE', NULL, 2, '2025-06-10 10:42:31', '2025-06-10 11:28:06', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (505, 505, '2025-06-10', 'Martes', '1:30 PM', '14.00', '14.00', '13.75', '0', 'MZA. C LOTE. 5 ASOC.HUERTA GRANJA (FRENTE A FAB.FAME/Y LOCAL ASOC. EL AYLLU) LIMA - LIMA - LURIGANCHO', NULL, 2, '2025-06-10 10:56:11', '2025-06-10 16:24:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (506, 506, '2025-06-11', 'Miércoles', '2:00 PM', '11.00', '11.00', '10.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 6, '2025-06-11 09:52:14', '2025-06-11 13:31:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (507, 507, '2025-06-12', 'Jueves', '2:00 PM', '10.00', '10.00', '9.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 6, '2025-06-12 08:53:35', '2025-06-12 13:57:48', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (508, 508, '2025-06-13', 'Viernes', '10:30 AM', '8.50', '8.50', '8.25', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 8', 6, '2025-06-12 16:13:05', '2025-06-13 09:43:08', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (509, 509, '2025-06-14', 'Sábado', '8:00 AM', '11.00', '11.00', '11', '0', 'HUAYCAN ZONA R', NULL, 6, '2025-06-13 17:46:02', '2025-06-14 10:00:20', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (511, 511, '2025-06-13', 'Viernes', '2:00 PM', '10.00', '10.00', '10.00', '0', 'CARAPONGO', NULL, 6, '2025-06-14 09:59:42', '2025-09-09 16:58:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (512, 512, '2025-06-13', 'Viernes', '2:00 PM', '9.00', '9.00', '9.00', '0', 'CERRO CAMOTE', NULL, 6, '2025-06-14 10:00:18', '2025-09-09 16:58:12', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (513, 513, '2025-06-17', 'Martes', '1:00 PM', '8.00', '8.00', '7.75', '0', 'AV. GIUSEPPE GARIBALDI 217- JESUS MARIA - LIMA', 'SLUMP 6', 6, '2025-06-14 13:24:17', '2025-06-17 12:35:51', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (514, 514, '2025-06-14', 'Sábado', '10:00 AM', '9.00', '9.00', '9.00', '0', 'SCANIA-HUACHIPA', NULL, 6, '2025-06-16 08:41:47', '2025-09-09 16:57:56', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (515, 515, '2025-06-17', 'Martes', '2:00 PM', '43.00', '43.00', '42', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 6, '2025-06-16 08:48:28', '2025-06-17 14:34:54', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (516, 516, '2025-06-16', 'Lunes', '3:00 PM', '7.00', '7.00', '6.75', '0', 'PARQUE DE LAS LEYENDAS - SAN MIGUEL', 'SLUMP 5', 2, '2025-06-16 14:07:13', '2025-06-16 14:48:49', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (517, 517, '2025-06-17', 'Martes', '4:00 PM', '6.00', '5.50', '5.25', '0', 'AV. TUCANES S/N SANTA MARIA DE HUACHIPA LURIGANCHO - LIMA', 'SLUMP 8 PV1 CAMPO FE', 2, '2025-06-17 12:12:30', '2025-06-17 17:00:59', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (518, 518, '2025-06-18', 'Miércoles', '9:30 AM', '7.00', '7.00', '6.75', '-1', 'CAMPO FE - LURIN', 'SLUMP 4-6 CIRCUITO RONEX', 2, '2025-06-17 16:51:42', '2025-06-18 09:53:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (520, 520, '2025-06-18', 'Miércoles', '2:00 PM', '11.00', '10.50', '10.25', '0', 'HUACHIPA', 'SLUMP 4-6', 2, '2025-06-18 15:26:00', '2025-06-18 15:31:55', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (521, 521, '2025-06-19', 'Jueves', '2:30 PM', '19.00', '19.00', '18.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', NULL, 6, '2025-06-18 16:36:14', '2025-06-19 14:30:01', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (522, 522, '2025-06-19', 'Jueves', '1:00 PM', '6.00', '6.00', '5.75', '0', 'CAMPO FE - LURIN', 'SLUMP 4-6', 2, '2025-06-19 13:14:50', '2025-06-19 13:48:29', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (523, 523, '2025-06-19', 'Jueves', '3:00 PM', '18.00', '14.5', '14.25', '0', 'EL AGUSTINO', 'PRECIO 255 X M3', 2, '2025-06-19 15:35:24', '2025-06-20 09:29:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (524, 524, '2025-06-20', 'Viernes', '11:00 AM', '17.00', '17.00', '16', '-0.5', 'AV GIUSEPPE GARIBALDI 209-217 LIMA-LIMA -JESUS MARIA', 'SLUMP 6', 6, '2025-06-19 15:49:47', '2025-06-20 10:50:21', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (525, 525, '2025-06-23', 'Lunes', '1:30 PM', '16.50', '16.50', '15.5', '-0.5', 'JR. HUASCAR 2162 - JESUS MARIA', '280 SLUMP 6-8  0.5', 6, '2025-06-23 08:38:27', '2025-06-23 13:15:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (526, 526, '2025-06-23', 'Lunes', '9:00 AM', '6.00', '5.5', '5.25', '0', 'PETRAMAS', NULL, 2, '2025-06-23 08:58:29', '2025-06-23 10:03:41', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (527, 527, '2025-06-23', 'Lunes', '2:00 PM', '16.00', '16.00', '15.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 4-6', 6, '2025-06-23 09:49:38', '2025-06-23 14:06:42', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (528, 528, '2025-06-24', 'Martes', '8:30 AM', '13.00', '13.00', '12', '0', 'HUACHIPA', NULL, 5, '2025-06-23 10:17:31', '2025-06-24 09:12:49', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (530, 530, '2025-06-25', 'Miércoles', '9:00 AM', '20.00', '19.00', '18.75', '0', 'HUACHIPA', 'SLUMP 4-6  (PRECIO 280 X M3)', 2, '2025-06-25 08:39:01', '2025-06-25 10:50:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (531, 531, '2025-06-25', 'Miércoles', '2:00 PM', '9.00', '9.00', '8.75', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP4-6', 6, '2025-06-25 10:59:48', '2025-06-26 10:20:07', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (532, 532, '2025-06-25', 'Miércoles', '12:00 PM', '1.00', '1.00', '1', '0', 'HUACHIPA', '280 + FLETE 100 SOLES', 2, '2025-06-25 12:59:13', '2025-06-26 09:57:44', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (534, 534, '2025-06-26', 'Jueves', '2:00 PM', '6.00', '5.00', '4.75', '0', 'CAMPO FE - LURIN', NULL, 2, '2025-06-26 08:46:35', '2025-06-26 12:50:05', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (535, 535, '2025-06-26', 'Jueves', '2:00 PM', '29.5', '27.00', '25.75', '-1', 'HUACHIPA NORTE', 'PRECIO 235 - BOMBA 600 - VIBRADORA 50', 5, '2025-06-26 10:04:57', '2025-06-27 09:39:22', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (536, 536, '2025-06-26', 'Jueves', '2:00 PM', '14.00', '14.00', '13.5', '0', 'JR. HUÁSCAR 2162 - JESUS MARIA', 'SLUMP 6-8', 6, '2025-06-26 10:19:20', '2025-06-26 13:51:57', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (538, 538, '2025-06-28', 'Sábado', '8:00 AM', '12.5', '11.00', '10.75', '0', 'BASILIO AUQUI- HUACHIPA', 'SLUMP 4-6', 2, '2025-06-27 11:31:52', '2025-06-28 09:27:28', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (539, 539, '2025-06-28', 'Sábado', '12:00 PM', '17.50', '17.50', '17.50', '0', 'CERRO CAMOTE', NULL, 5, '2025-06-27 17:36:35', '2025-06-28 13:16:50', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (540, 540, '2025-06-28', 'Sábado', '8:00 AM', '6.00', '5.50', '5.25', '0', 'PETRAMAS', NULL, 2, '2025-06-27 17:49:37', '2025-06-28 09:03:53', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (542, 542, '2025-06-28', 'Sábado', '12:00 PM', '21.00', '20.00', '19.75', '-2.5', 'LA VICTORIA', '(270)', 2, '2025-06-28 08:27:40', '2025-06-30 09:04:00', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (543, 543, '2025-06-28', 'Sábado', '2:00 PM', '18.00', '16.00', '15.75', '0', 'CAJAMARQUILLA', NULL, 2, '2025-06-28 08:29:02', '2025-06-28 16:30:04', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (544, 544, '2025-06-28', 'Sábado', '1:00 PM', '8.00', '8.00', '7.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA , LIMA- LIMA-LURIGANCHO', 'SLUMP 8 NAVE J1', 2, '2025-06-28 08:41:19', '2025-06-28 14:53:58', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (545, 545, '2025-06-27', 'Viernes', '10:00 AM', '7.00', '7.00', '7.00', '0', 'SANTA ANITA', '(550)', 6, '2025-06-28 11:02:14', '2025-09-09 16:52:14', 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (546, 546, '2025-06-30', 'Lunes', '10:30 AM', '10.50', '10.50', '10.25', '0', 'JR. HUASCAR 2162 ,LIMA-LIMA -LIMA', NULL, 6, '2025-06-30 09:30:04', '2025-06-30 09:51:04', 'S', '15', '1501', '150101', 'LIMA', 'LIMA', 'LIMA', 'JR. HUASCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (547, 547, '2025-06-30', 'Lunes', '2:00 PM', '6.50', '6.5', '6.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-01 09:22:46', '2025-07-01 16:05:08', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (548, 548, '2025-07-01', 'Martes', '3:00 PM', '9.50', '9.50', '16', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-01 09:29:40', '2025-07-02 10:53:58', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (549, 549, '2025-06-30', 'Lunes', '1:30 PM', '8.50', '8.50', '8.25', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', 'SLUMP 6.8', 2, '2025-07-01 11:02:22', '2025-07-01 16:03:53', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (550, 550, '2025-07-01', 'Martes', '1:30 PM', '12.00', '12.00', '11.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-01 11:34:09', '2025-07-01 14:15:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (551, 551, '2025-06-28', 'Sábado', '8:00 AM', '6.00', '6.00', '6.00', '0', 'CAMPOY,LIMA-LIMA -EL AGUSTINO', NULL, 6, '2025-07-01 16:38:04', '2025-09-09 16:51:52', 'S', '15', '1501', '150111', 'LIMA', 'LIMA', 'EL AGUSTINO', 'CAMPOY', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (552, 552, '2025-07-02', 'Miércoles', '10:00 AM', '12.00', '12.00', '11.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-02 09:28:57', '2025-07-02 11:25:59', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (553, 553, '2025-07-02', 'Miércoles', '3:00 PM', '22.50', '22.50', '22', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -', 'SLUMP 4-6 - FREC 30 MIN', 6, '2025-07-02 10:29:53', '2025-07-02 14:47:30', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', NULL, 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (554, 554, '2025-07-02', 'Miércoles', '2:00 PM', '14.00', '14.00', '14.00', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', '550', 5, '2025-07-02 13:18:19', '2025-08-04 12:53:41', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (555, 555, '2025-07-03', 'Jueves', '10:00 AM', '12.00', '12.00', '11.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-03 10:22:19', '2025-07-03 11:08:14', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (556, 556, '2025-07-03', 'Jueves', '2:00 PM', '11.50', '11.50', '11.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-03 11:05:17', '2025-07-03 13:23:15', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (557, 557, '2025-07-04', 'Viernes', '1:00 PM', '19.50', '16.50', '16.25', '0', 'CERRO CAMOTE,LIMA-LIMA -LURIGANCHO', 'BOMBA 550 - VIBRADORA 50', 5, '2025-07-03 17:41:56', '2025-07-05 11:45:40', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CERRO CAMOTE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (558, 558, '2025-07-04', 'Viernes', '11:00 AM', '8.00', '7.00', '6.75', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-04 08:28:42', '2025-07-04 11:15:52', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (559, 559, '2025-07-04', 'Viernes', '2:00 PM', '12.50', '12.50', '12.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-04 10:52:35', '2025-07-04 13:24:22', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (560, 560, '2025-07-04', 'Viernes', '4:00 PM', '2.00', '2.00', '2.00', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'FLETE 130', NULL, '2025-07-04 16:23:37', '2025-07-04 16:44:47', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (561, 561, '2025-07-05', 'Sábado', '9:30 AM', '15.00', '15.00', '14.75', '0', 'JR.LIBERTAD 420 - MATUCANA,LIMA-LIMA -LIMA', 'SLUMP 6-8', NULL, '2025-07-04 16:28:11', '2025-07-05 06:09:21', 'S', '15', '1501', '150101', 'LIMA', 'LIMA', 'LIMA', 'JR.LIBERTAD 420 - MATUCANA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (562, 562, '2025-07-05', 'Sábado', '7:30 AM', '20.00', '16.50', '16.25', '0', 'HUAYCAN,LIMA-LIMA -ATE', '260', 2, '2025-07-04 16:32:18', '2025-07-05 11:48:37', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (563, 563, '2025-07-05', 'Sábado', '9:00 AM', '16.5', '14.00', '13.75', '0', 'HUAYCAN,LIMA-LIMA -ATE', '260', 2, '2025-07-04 16:33:52', '2025-07-05 11:48:26', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (564, 564, '2025-07-05', 'Sábado', '2:00 PM', '14.50', '12.50', '12.25', '0', 'HUAYCAN,LIMA-LIMA -ATE', NULL, 2, '2025-07-04 16:35:07', '2025-07-05 17:02:24', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (565, 565, '2025-07-05', 'Sábado', '1:00 PM', '22.5', '21.5', '21.50', '0', 'ASOCIACION PRIALE,LIMA-LIMA -ATE', '245', 7, '2025-07-05 08:46:11', '2025-07-08 09:48:16', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'ASOCIACION PRIALE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (566, 566, '2025-07-05', 'Sábado', '11:00 AM', '14.50', '12.00', '11.75', '0', 'LAS LOMAS CERRO CAMOTE,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-05 11:41:14', '2025-07-05 15:02:24', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'LAS LOMAS CERRO CAMOTE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (567, 567, '2025-07-05', 'Sábado', '12:00 PM', '8.00', '6.00', '5.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-05 12:49:10', '2025-07-05 13:05:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (568, 568, '2025-07-07', 'Lunes', '10:00 AM', '18.00', '16.00', '15.75', '0', 'SANTA CRUZ CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-07 10:37:01', '2025-07-07 11:37:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'SANTA CRUZ CAJAMARQUILLA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (569, 569, '2025-07-07', 'Lunes', '2:00 PM', '14.00', '14.00', '14.00', '0', 'CHACLACAYO,LIMA-LIMA -CHACLACAYO', '550 + 5 TUBOS ADICIONALES', 7, '2025-07-07 15:06:30', '2025-08-04 13:07:16', 'S', '15', '1501', '150107', 'LIMA', 'LIMA', 'CHACLACAYO', 'CHACLACAYO', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (570, 570, '2025-07-08', 'Martes', '2:00 PM', '20', '20', '27.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-07 17:25:36', '2025-07-08 17:24:47', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (571, 571, '2025-07-08', 'Martes', '1:00 PM', '13.00', '13.00', '12.75', '0', 'CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', '285', 2, '2025-07-08 12:06:37', '2025-07-08 14:14:30', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (572, 572, '2025-07-09', 'Miércoles', '3:00 PM', '10.00', '10.00', '9.75', '0', 'CAMPO FE HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-07-08 16:41:37', '2025-07-09 15:30:35', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPO FE HUACHIPA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (573, 573, '2025-07-09', 'Miércoles', '8:00 AM', '9.00', '9.00', '8.75', '0', 'COORPORATIVA MANILSA MZ U LT 1,LIMA-LIMA -ATE', '244.444 + 300 BOMB - LUIS CHAVEZ', 6, '2025-07-08 16:43:40', '2025-07-09 08:43:44', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'COORPORATIVA MANILSA MZ U LT 1', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (574, 574, '2025-07-09', 'Miércoles', '2:00 PM', '8.50', '8.50', '8.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-07-09 12:48:00', '2025-07-09 13:15:47', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (575, 575, '2025-07-09', 'Miércoles', '2:30 PM', '13.50', '13.50', '13.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-09 12:49:06', '2025-07-09 13:50:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (576, 576, '2025-07-11', 'Viernes', '9:30 AM', '14.50', '14.50', '14.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-10 16:07:13', '2025-07-11 08:45:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (578, 578, '2025-07-12', 'Sábado', '9:00 AM', '8.00', '8.00', '7.75', '0', 'CAMPO FE HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-07-11 12:46:37', '2025-07-12 09:03:10', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPO FE HUACHIPA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (579, 579, '2025-07-12', 'Sábado', '8:30 AM', '20.00', '18.50', '18.5', '0', 'ASOCIACION PRIALE,LIMA-LIMA -ATE', '245 + BOMB 600 + REGL 50 / ADELANTO 500', 7, '2025-07-11 12:48:50', '2025-07-12 09:50:58', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'ASOCIACION PRIALE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (581, 581, '2025-07-12', 'Sábado', '12:00 PM', NULL, NULL, '1', '0', 'HORACIO ZEVALLOS,LIMA-LIMA -ATE', NULL, 6, '2025-07-11 17:42:58', '2025-07-12 13:54:21', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HORACIO ZEVALLOS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (582, 582, '2025-07-12', 'Sábado', '12:00 PM', '17.50', '15.00', '14.75', '0', 'HUAYCAN,LIMA-LIMA -ATE', NULL, 2, '2025-07-12 08:49:47', '2025-07-14 08:38:04', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (583, 583, '2025-07-12', 'Sábado', '2:30 PM', '15.5', '13', '12.75', '0', 'HORACIO ZEVALLOS ,LIMA-LIMA -ATE', NULL, 2, '2025-07-12 08:51:35', '2025-07-14 08:41:13', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HORACIO ZEVALLOS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (584, 584, '2025-07-12', 'Sábado', '3:00 PM', '12.00', '10.00', '9.75', '0', 'CAMPOY,LIMA-LIMA -EL AGUSTINO', NULL, 2, '2025-07-12 08:56:08', '2025-07-12 17:56:56', 'S', '15', '1501', '150111', 'LIMA', 'LIMA', 'EL AGUSTINO', 'CAMPOY', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (585, 585, '2025-07-11', 'Viernes', '2:00 PM', '14.00', '14.00', '14.00', '0', 'SURCO,LIMA-LIMA -LIMA', NULL, 6, '2025-07-12 10:13:07', '2025-08-04 13:09:14', 'S', '15', '1501', '150101', 'LIMA', 'LIMA', 'LIMA', 'SURCO', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (586, 586, '2025-07-12', 'Sábado', '10:00 AM', NULL, NULL, '1', '0', 'SAN JUAN,LIMA-LIMA -ATE', NULL, 6, '2025-07-12 12:27:16', '2025-07-12 13:53:50', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'SAN JUAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (587, 587, '2025-07-14', 'Lunes', '8:00 AM', '105.00', '98.00', '96.75', '0.00', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'ESTRUCTURA PISO', 2, '2025-07-12 15:23:44', '2025-08-04 13:10:26', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (588, 588, '2025-07-14', 'Lunes', '1:00 PM', '23.00', '23.00', '22.5', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-14 11:28:58', '2025-07-14 12:43:57', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (589, 589, '2025-07-16', 'Miércoles', '10:00 AM', '22.00', '22.00', '21.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-14 11:30:22', '2025-07-16 12:33:08', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (590, 590, '2025-07-14', 'Lunes', '4:00 PM', '3', '2', '2.00', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', NULL, NULL, '2025-07-14 16:33:30', '2025-07-21 10:37:22', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (591, 591, '2025-07-15', 'Martes', '8:30 AM', '32.00', '27.00', '26.75', '0', 'HUAYCAN,LIMA-LIMA -ATE', NULL, 2, '2025-07-15 08:10:49', '2025-07-15 09:50:27', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (592, 592, '2025-07-15', 'Martes', '12:00 PM', '4.00', '4.00', '4.00', '0', 'SANTA CLARA,LIMA-LIMA -ATE', NULL, 6, '2025-07-15 14:00:53', '2025-08-04 13:11:22', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'SANTA CLARA', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (593, 593, '2025-07-16', 'Miércoles', '11:30 AM', '6.00', '6.00', '5.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-07-15 17:57:42', '2025-07-16 12:33:29', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (594, 594, '2025-07-16', 'Miércoles', '1:00 PM', '10.00', '9.50', '9.50', '0', 'CAMPO FE HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 5, '2025-07-16 09:49:54', '2025-07-16 13:32:56', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPO FE HUACHIPA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (596, 596, '2025-07-17', 'Jueves', '10:30 AM', '13.50', '13.50', '13.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-16 17:07:43', '2025-07-17 11:39:13', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (597, 597, '2025-07-17', 'Jueves', '2:30 PM', '14.50', '14.50', '14.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-07-16 17:08:34', '2025-07-17 14:10:41', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (598, 598, '2025-07-17', 'Jueves', '10:00 PM', '6.50', '6.50', '6.25', '0', 'MZA. C LOTE. 5 ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', 2, '2025-07-17 11:10:18', '2025-07-17 11:56:32', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ASOC.HUERTA GRANJA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (599, 599, '2025-07-18', 'Viernes', '2:30 PM', '16.00', '16.00', '15.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-17 17:47:16', '2025-07-18 14:10:19', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (600, 600, '2025-07-18', 'Viernes', '10:30 PM', '7.00', '7.00', '6.75', '0', 'MZA. C LOTE. 5 ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-18 11:03:00', '2025-07-18 11:55:02', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ASOC.HUERTA GRANJA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (601, 601, '2025-07-18', 'Viernes', '2:00 PM', '8.00', '8.00', '7.75', '0', 'CAMPO FE HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-07-18 11:49:22', '2025-07-18 15:51:42', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPO FE HUACHIPA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (603, 603, '2025-07-19', 'Sábado', '10:00 AM', '11.50', '11.50', '11.25', '0', 'SURCO,LIMA-LIMA -SANTIAGO DE SURCO', 'SLUMP 4-6 / 253+600', 6, '2025-07-18 15:04:31', '2025-07-19 13:01:13', 'S', '15', '1501', '150140', 'LIMA', 'LIMA', 'SANTIAGO DE SURCO', 'SURCO', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (604, 604, '2025-07-19', 'Sábado', '8:00 AM', '28.00', '25.00', '24.75', '0', 'CABALLO BLANCO NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-18 15:05:54', '2025-07-19 10:33:16', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CABALLO BLANCO NIEVERIA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (605, 605, '2025-07-19', 'Sábado', '8:00 AM', '6.00', '2.00', '2', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-18 17:23:44', '2025-07-19 09:17:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (607, 607, '2025-07-21', 'Lunes', '10:30 AM', '34.50', '34.50', '34.00', '-1.75', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-19 13:55:41', '2025-09-09 16:06:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (608, 608, '2025-07-21', 'Lunes', '10:30 AM', '7.00', '7.00', '6.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-21 09:48:02', '2025-07-21 13:11:26', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (609, 609, '2025-07-22', 'Martes', '1:00 PM', '6.50', '6.50', '6.5', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-22 10:29:37', '2025-07-22 13:59:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (610, 610, '2025-07-22', 'Martes', '10:30 PM', '8.00', '8.00', '8.00', '0', 'SAN MIGUEL,LIMA-LIMA -SAN MIGUEL', '600', 6, '2025-07-22 11:24:10', '2025-08-04 13:16:15', 'S', '15', '1501', '150136', 'LIMA', 'LIMA', 'SAN MIGUEL', 'SAN MIGUEL', '0.00', NULL);
INSERT INTO `administracion_programacion` VALUES (611, 611, '2025-07-24', 'Jueves', '10:00 AM', '7.00', '7.00', '6.75', '0', 'C. AMADOR MERINO REYNA 346,LIMA-LIMA -SAN ISIDRO', 'PIEDRA 57 SLUMP B  / 267', 2, '2025-07-22 16:59:27', '2025-07-26 13:55:20', 'S', '15', '1501', '150131', 'LIMA', 'LIMA', 'SAN ISIDRO', 'C. AMADOR MERINO REYNA 346', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (612, 612, '2025-07-24', 'Jueves', '9:30 AM', '25.00', '25.00', '24.50', '0.00', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-24 08:14:41', '2025-08-04 13:23:33', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (613, 613, '2025-07-24', 'Jueves', '11:30 AM', '16.00', '16.00', '15.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-24 09:17:59', '2025-07-24 10:48:42', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (614, 614, '2025-07-25', 'Viernes', '10:30 AM', '28.00', '26.00', '25.75', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6 / 315', 2, '2025-07-24 14:01:19', '2025-07-25 13:44:37', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (615, 615, '2025-07-24', 'Jueves', '4:00 PM', '6.00', '4.00', '3.75', '0', 'LURIN MAESC,LIMA-LIMA -LURIN', 'SLUMP 4-6', 2, '2025-07-24 15:20:04', '2025-07-24 15:42:43', 'S', '15', '1501', '150119', 'LIMA', 'LIMA', 'LURIN', 'LURIN MAESC', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (616, 616, '2025-07-25', 'Viernes', '9:30 AM', '14.00', '14.00', '13.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-24 17:41:58', '2025-07-25 09:10:27', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (617, 617, '2025-07-25', 'Viernes', '2:30 PM', '15.00', '15.00', '14.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-07-24 18:04:46', '2025-07-25 14:20:10', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (618, 618, '2025-07-25', 'Viernes', '4:00 PM', '6.00', '5.00', '4.75', '0', 'CAMPO FE ,LIMA-LIMA -LURIN', NULL, 2, '2025-07-25 08:51:57', '2025-07-25 15:44:13', 'S', '15', '1501', '150119', 'LIMA', 'LIMA', 'LURIN', 'CAMPO FE', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (619, 619, '2025-07-26', 'Sábado', '2:00 PM', '21.50', '19.50', '19', '0', 'CURVA DE CHAMBALA ,LIMA-LIMA -LURIGANCHO', NULL, 7, '2025-07-25 09:58:53', '2025-07-26 15:20:03', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CURVA DE CHAMBALA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (620, 620, '2025-07-26', 'Sábado', '8:00 AM', '35.50', '35.50', '35.5', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', '235 ALEX ALVARES', 6, '2025-07-25 11:44:20', '2025-07-26 10:29:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (621, 621, '2025-07-26', 'Sábado', '10:30 AM', '7.50', '7.50', '7.25', '0', 'SURCO,LIMA-LIMA -SANTIAGO DE SURCO', 'SLUMP 4-6  / 248 KATERINE', 2, '2025-07-25 12:31:24', '2025-07-26 09:41:58', 'S', '15', '1501', '150140', 'LIMA', 'LIMA', 'SANTIAGO DE SURCO', 'SURCO', '0', NULL);
INSERT INTO `administracion_programacion` VALUES (622, 622, '2025-07-26', 'Sábado', '1:00 PM', '73.00', '70.00', '69.50', '0.00', 'BAYOBAR,LIMA-LIMA -SAN JUAN DE LURIGANCHO', NULL, 6, '2025-07-26 12:09:47', '2025-08-04 13:20:20', 'S', '15', '1501', '150132', 'LIMA', 'LIMA', 'SAN JUAN DE LURIGANCHO', 'BAYOBAR', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (623, 623, '2025-07-30', 'Miércoles', '10:00 AM', '34.50', '34.50', '34.00', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-30 08:44:52', '2025-07-30 10:19:40', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '9', 'M3');
INSERT INTO `administracion_programacion` VALUES (624, 624, '2025-07-30', 'Miércoles', '11:30 AM', '9.00', '9.00', '8.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-07-30 08:45:44', '2025-07-30 11:55:33', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '9.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (625, 625, '2025-07-31', 'Jueves', '2:30 PM', '6.00', '5.50', '5.25', '0.00', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-07-30 14:33:58', '2025-07-31 16:53:58', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (626, 626, '2025-07-31', 'Jueves', '1:00 PM', '10.50', '10.50', '10.25', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-07-30 14:35:16', '2025-07-31 13:37:06', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '10.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (627, 627, '2025-08-01', 'Viernes', '10:30 AM', '23.00', '23.00', '22.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-07-31 08:45:28', '2025-08-01 10:14:39', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (628, 628, '2025-08-01', 'Viernes', '8:00 AM', '17.50', '15.50', '15.25', '0', 'CAMPOY,LIMA-LIMA -LURIGANCHO', '255', 6, '2025-07-31 16:58:09', '2025-08-01 12:02:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPOY', '17.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (629, 629, '2025-08-01', 'Viernes', '3:30 PM', '6.00', '3.00', '3.00', '0', 'CAMPO FE ,LIMA-LIMA -LURIN', NULL, 2, '2025-08-01 09:58:02', '2025-08-01 15:13:31', 'S', '15', '1501', '150119', 'LIMA', 'LIMA', 'LURIN', 'CAMPO FE', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (630, 630, '2025-08-01', 'Viernes', '12:30 PM', '10.00', '10.00', '9.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-01 12:30:12', '2025-08-01 14:43:42', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (631, 631, '2025-08-01', 'Viernes', '2:00 PM', '7.00', '7.00', '7.00', '0', 'SAN JUAN DE LURIGANCHO,LIMA-LIMA -SAN JUAN DE LURIGANCHO', NULL, 6, '2025-08-01 14:53:51', '2025-08-04 13:21:35', 'S', '15', '1501', '150132', 'LIMA', 'LIMA', 'SAN JUAN DE LURIGANCHO', 'SAN JUAN DE LURIGANCHO', NULL, 'M3');
INSERT INTO `administracion_programacion` VALUES (632, 632, '2025-08-04', 'Lunes', '2:00 PM', '8.50', '8.50', '8.25', '0.00', 'SURCO,LIMA-LIMA -SANTIAGO DE SURCO', 'SLUMP 4-6     253 X M3 - BOMBA TERESA', 1, '2025-08-01 14:56:29', '2025-08-05 09:32:59', 'S', '15', '1501', '150140', 'LIMA', 'LIMA', 'SANTIAGO DE SURCO', 'SURCO', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (633, 633, '2025-08-02', 'Sábado', '10:00 AM', '13.50', '13.50', '13.50', '0', 'ATE,LIMA-LIMA -ATE', NULL, 6, '2025-08-01 16:13:12', '2025-08-04 13:22:36', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'ATE', NULL, 'M3');
INSERT INTO `administracion_programacion` VALUES (634, 634, '2025-08-02', 'Sábado', '1:00 PM', '14.50', '12.50', '12.25', '0', 'LAS AMERICAS,LIMA-LIMA -ATE', 'SLUMP 6-8', 2, '2025-08-01 17:25:01', '2025-08-02 13:15:00', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'LAS AMERICAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (635, 635, '2025-08-04', 'Lunes', '10:30 AM', '23.50', '23.50', '23.00', '0.00', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-04 08:41:51', '2025-08-11 15:07:52', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '23.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (636, 636, '2025-08-04', 'Lunes', '2:30 PM', '13.00', '13.00', '12.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-04 08:44:23', '2025-08-04 14:09:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '13.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (637, 637, '2025-08-05', 'Martes', '10:30 AM', '26.00', '26.00', '25.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-05 08:35:52', '2025-08-05 10:50:56', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '26.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (638, 638, '2025-08-05', 'Martes', '2:30 PM', '12.50', '12.50', '12.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-05 08:36:44', '2025-08-05 13:56:13', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '12.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (639, 639, '2025-08-05', 'Martes', '4:30 PM', '6.00', '5.00', '4.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', 2, '2025-08-05 16:15:33', '2025-08-05 16:50:59', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (640, 640, '2025-08-07', 'Jueves', '10:30 AM', '17.00', '17.00', '16.75', '0.00', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-07 08:47:31', '2025-09-09 16:07:39', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '5.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (641, 641, '2025-08-07', 'Jueves', '2:30 PM', '8.00', '8.00', '7.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-07 08:48:26', '2025-08-07 13:48:28', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (642, 642, '2025-08-07', 'Jueves', '4:30 PM', '6.00', '6.00', '5.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6 PLATAFORMA DE COMEDOR ACH/SARDINEL', 2, '2025-08-07 09:05:07', '2025-08-07 16:42:07', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (643, 643, '2025-08-07', 'Jueves', '2:00 PM', '6.00', '6.00', '5.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-07 09:42:13', '2025-08-07 14:22:18', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (644, 644, '2025-08-08', 'Viernes', '2:00 PM', '10.00', '10.00', '9.75', '0', 'CAJAMARQUILLA,LIMA-LIMA -LIMA', NULL, 2, '2025-08-08 09:24:33', '2025-08-08 14:35:11', 'S', '15', '1501', '150101', 'LIMA', 'LIMA', 'LIMA', 'CAJAMARQUILLA', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (645, 645, '2025-08-08', 'Viernes', '12:00 PM', '16.00', '12.00', '11.75', '0', 'HUAYCAN ,LIMA-LIMA -ATE', NULL, 2, '2025-08-08 11:11:04', '2025-08-08 12:12:31', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (646, 646, '2025-08-08', 'Viernes', '2:30 PM', '10.00', '10.00', '9.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-08 13:16:42', '2025-08-08 13:52:52', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (647, 647, '2025-08-08', 'Viernes', '4:00 PM', '6.00', '2.00', '2.00', '0', 'HUACHIPA ,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-08 16:30:18', '2025-08-08 17:00:52', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (648, 648, '2025-08-09', 'Sábado', '8:00 AM', '18.50', '16.50', '16.25', '0', 'LAS AMERICAS ,LIMA-LIMA -LURIGANCHO', NULL, NULL, '2025-08-08 17:54:20', '2025-08-09 08:09:53', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'LAS AMERICAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (649, 649, '2025-08-08', 'Viernes', '8:00 AM', '0', '0', '0', '0', 'HUACHIPA ,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-08-08 17:57:01', '2025-08-08 17:57:01', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (650, 650, '2025-08-09', 'Sábado', '8:00 AM', '8.00', '8.00', '8.00', '0', 'HUACHIPA ,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-08-08 18:00:28', '2025-09-09 16:39:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (651, 651, '2025-08-11', 'Lunes', '9:30 AM', '27.50', '27.50', '27', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-11 08:14:27', '2025-08-11 09:45:51', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '27.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (652, 652, '2025-08-11', 'Lunes', '8:00 AM', '13.50', '12.00', '11.75', '0.00', 'PLANTA AC-001,LIMA-LIMA -ATE', 'VENTA SOLO CONCRETO', 2, '2025-08-11 09:24:41', '2025-08-11 12:08:20', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'PLANTA AC-001', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (653, 653, '2025-08-11', 'Lunes', '11:00 AM', '26.00', '23.50', '23.25', '0', 'CAMPOY,LIMA-LIMA -SAN JUAN DE LURIGANCHO', NULL, 7, '2025-08-11 10:37:16', '2025-08-11 13:38:21', 'S', '15', '1501', '150132', 'LIMA', 'LIMA', 'SAN JUAN DE LURIGANCHO', 'CAMPOY', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (654, 654, '2025-08-11', 'Lunes', '11:30 AM', '13.00', '11.00', '10.75', '0', 'PLATA AC 001,LIMA-LIMA -ATE', 'VENTA SOLO CONCRETO', 2, '2025-08-11 12:07:46', '2025-08-11 12:55:21', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'PLATA AC 001', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (655, 655, '2025-08-11', 'Lunes', '3:00 PM', '8.00', '7.00', '6.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, NULL, '2025-08-11 14:43:52', '2025-08-11 15:02:14', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (657, 657, '2025-08-11', 'Lunes', '3:00 PM', '14.00', '10.00', '10.00', '0.00', 'VALLE GRANDE ,LIMA-LIMA -ATE', 'UNIDAD PROPIA', 2, '2025-08-11 16:12:55', '2025-09-09 16:08:26', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'VALLE GRANDE', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (658, 658, '2025-08-12', 'Martes', '10:00 AM', '26.50', '26.50', '26.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-12 08:55:10', '2025-08-12 10:09:47', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '26.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (659, 659, '2025-08-12', 'Martes', '2:30 PM', '9.50', '9.50', '9.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-12 08:56:27', '2025-08-12 13:52:22', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '9.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (660, 660, '2025-08-12', 'Martes', '10:30 AM', '5.00', '5.00', '4.75', '0', 'CAJAMARQUILLA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4 - 6', 2, '2025-08-12 10:34:50', '2025-08-12 11:40:18', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '5.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (661, 661, '2025-08-13', 'Miércoles', '8:00 AM', '20.00', '20.00', '19.75', '0', 'RICARDO PALMA CHOSICA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6 / 275+IGV / BOMBA 700', 6, '2025-08-12 14:26:43', '2025-08-13 07:24:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'RICARDO PALMA CHOSICA', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (662, 662, '2025-08-13', 'Miércoles', '10:00 AM', '5.00', '4.00', '3.75', '0', 'RICARDO PALMA CHOSICA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6 +FLETE 1 CUBO', 2, '2025-08-13 10:57:59', '2025-08-13 11:35:48', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'RICARDO PALMA CHOSICA', '5.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (663, 663, '2025-08-13', 'Miércoles', '2:00 PM', '11.50', '11.50', '11.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-13 12:09:48', '2025-08-13 13:55:37', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '11.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (664, 664, '2025-08-13', 'Miércoles', '1:30 PM', '8.00', '8.00', '8.00', '0', 'ATE,LIMA-LIMA -ATE', NULL, 7, '2025-08-13 14:31:09', '2025-09-09 16:38:27', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'ATE', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (665, 665, '2025-08-14', 'Jueves', '10:30 AM', '29.50', '29.50', '29.00', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-14 08:40:45', '2025-08-14 10:17:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '29.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (666, 666, '2025-08-14', 'Jueves', '2:00 PM', '10.00', '9.00', '8.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-14 12:31:33', '2025-08-14 14:38:21', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (667, 667, '2025-08-15', 'Viernes', '2:30 PM', '12.50', '12.50', '12.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-08-14 16:42:11', '2025-08-15 13:56:16', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '12.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (668, 668, '2025-08-16', 'Sábado', '8:00 AM', '10.00', '10.00', '9.75', '0.00', 'CAMPO FE ,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-08-15 12:37:03', '2025-08-16 08:58:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAMPO FE', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (669, 669, '2025-08-18', 'Lunes', '10:00 AM', '44.50', '44.50', '44.00', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-08-18 08:52:22', '2025-08-18 11:33:38', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '44.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (670, 670, '2025-08-19', 'Martes', '2:00 PM', '20.50', '20.50', '19.50', '0.00', 'CARAPONGO,LIMA-LIMA -LURIGANCHO', '245+550BOMBA + 50 REGLERO', 4, '2025-08-19 11:10:24', '2025-08-19 15:32:23', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CARAPONGO', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (671, 671, '2025-08-19', 'Martes', '2:30 PM', '9.50', '9.50', '9.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-19 12:05:37', '2025-08-19 13:47:37', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (672, 672, '2025-08-23', 'Sábado', '8:00 AM', '30.00', '30.00', '30.00', '0.00', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', '235XM3/ BOMBA 25XM3', 6, '2025-08-19 15:26:37', '2025-09-09 16:14:46', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (673, 673, '2025-08-20', 'Miércoles', '8:30 AM', '74.00', '68.50', '66.50', '0.00', 'SANTA CLARA,LIMA-LIMA -ATE', NULL, 7, '2025-08-19 18:29:36', '2025-09-09 16:18:13', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'SANTA CLARA', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (674, 674, '2025-08-20', 'Miércoles', '10:30 AM', '26.00', '26.00', '25.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-20 08:54:07', '2025-08-20 10:16:08', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '13', 'M3');
INSERT INTO `administracion_programacion` VALUES (675, 675, '2025-08-20', 'Miércoles', '2:30 PM', NULL, NULL, '0.00', '0.00', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SE CANCELO DESPACHO', NULL, '2025-08-20 08:55:01', '2025-08-21 09:15:54', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '9.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (676, 676, '2025-08-21', 'Jueves', '2:30 PM', '8.50', '8.50', '8.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-21 11:08:05', '2025-08-21 13:44:14', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (677, 677, '2025-08-21', 'Jueves', '3:00 PM', '10.00', '10.00', '9.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-21 11:09:03', '2025-08-21 14:08:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (678, 678, '2025-08-22', 'Viernes', '2:00 PM', '23.50', '21.50', '21.00', '0.00', 'EL CONDOMINIO DE ENSUEÑO,LIMA-LIMA -LURIGANCHO', NULL, 4, '2025-08-21 16:52:02', '2025-08-22 15:35:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'EL CONDOMINIO DE ENSUEÑO', '23.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (679, 679, '2025-08-22', 'Viernes', '10:30 AM', '18.50', '18.50', '18.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-22 09:02:28', '2025-08-22 10:04:10', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '18.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (680, 680, '2025-08-22', 'Viernes', '10:00 PM', '2.00', '2.00', '2.00', '0', 'ACH,LIMA-LIMA -LURIGANCHO', 'SLUMP 6-7', 2, '2025-08-22 10:53:27', '2025-08-22 11:12:48', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'ACH', '2.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (681, 681, '2025-08-22', 'Viernes', '1:00 PM', '18.50', '16.50', '16.25', '0.00', 'SANTA CLARA,LIMA-LIMA -ATE', NULL, 2, '2025-08-22 13:24:51', '2025-09-09 16:16:56', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'SANTA CLARA', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (682, 682, '2025-08-23', 'Sábado', '8:00 AM', '20.00', '16.00', '15.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, NULL, '2025-08-22 18:14:51', '2025-08-23 09:26:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (683, 683, '2025-08-23', 'Sábado', '10:00 AM', '10.50', '9.00', '8.75', '0', 'LAS LOMAS,LIMA-LIMA -LURIGANCHO', '263XM3', 2, '2025-08-23 09:41:18', '2025-08-23 11:08:35', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'LAS LOMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (684, 684, '2025-08-25', 'Lunes', '10:00 AM', '6.00', '6.00', '5.75', '0', 'RICARDO PALMA,LIMA-HUAROCHIRÍ -RICARDO PALMA', NULL, 2, '2025-08-23 10:43:06', '2025-08-25 09:21:24', 'S', '15', '1507', '150714', 'LIMA', 'HUAROCHIRÍ', 'RICARDO PALMA', 'RICARDO PALMA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (685, 685, '2025-08-23', 'Sábado', '12:00 PM', '2.00', '2.00', '2.00', '0.00', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-23 12:47:57', '2025-08-25 11:57:55', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (686, 686, '2025-08-25', 'Lunes', '9:00 AM', '10.00', '10.00', '9.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-23 13:51:05', '2025-08-25 09:05:59', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (687, 687, '2025-08-25', 'Lunes', '9:30 PM', '12.00', '12.00', '11.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-25 08:32:59', '2025-08-25 09:46:39', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (688, 688, '2025-08-25', 'Lunes', '10:30 AM', '8.00', '8.00', '7.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-25 08:33:59', '2025-08-25 10:04:34', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (689, 689, '2025-08-25', 'Lunes', '11:00 AM', '8.00', '8.00', '7.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-25 08:35:41', '2025-08-25 10:25:23', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (690, 690, '2025-08-23', 'Sábado', '11:00 AM', '2.00', '2.00', '2', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, NULL, '2025-08-25 11:59:00', '2025-08-25 12:00:12', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '2.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (691, 691, '2025-08-25', 'Lunes', '2:00 PM', '7.00', '7.00', '6.75', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 2, '2025-08-25 12:43:28', '2025-08-25 13:39:19', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '7.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (692, 692, '2025-08-26', 'Martes', '2:30 PM', '8.00', '8.00', '7.75', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-26 09:19:14', '2025-08-26 13:43:01', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (693, 693, '2025-08-26', 'Martes', '4:00 PM', '2.50', '2.50', '2.50', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA ,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-26 14:32:10', '2025-08-26 16:09:34', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '2.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (694, 694, '2025-08-27', 'Miércoles', '10:30 AM', '26.50', '26.50', '26.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-27 08:36:14', '2025-08-27 09:56:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '26.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (695, 695, '2025-08-27', 'Miércoles', '1:00 PM', '11.00', '11.00', '10.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-27 11:40:52', '2025-08-27 13:31:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '11.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (696, 696, '2025-08-27', 'Miércoles', '4:00 PM', '12.00', '12.00', '11.75', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-08-27 17:13:02', '2025-08-27 17:41:24', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (697, 697, '2025-08-28', 'Jueves', '11:00 AM', '20.50', '20.50', '20.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-28 08:53:06', '2025-08-28 10:07:47', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '20.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (698, 698, '2025-08-28', 'Jueves', '2:30 PM', '11.50', '11.50', '11.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-28 08:53:52', '2025-08-28 14:06:16', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '11.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (699, 699, '2025-08-29', 'Viernes', '2:00 PM', '26.00', '24.00', '23.50', '0.00', 'RICARDO PALMA,LIMA-HUAROCHIRÍ -RICARDO PALMA', 'SLUMP 4-6', 4, '2025-08-28 14:26:49', '2025-09-02 11:10:04', 'S', '15', '1507', '150714', 'LIMA', 'HUAROCHIRÍ', 'RICARDO PALMA', 'RICARDO PALMA', '26.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (700, 700, '2025-08-29', 'Viernes', '8:30 AM', '19.00', '17.00', '16.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', 2, '2025-08-28 17:57:18', '2025-08-29 09:27:27', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (701, 701, '2025-08-29', 'Viernes', '11:00 AM', '18.00', '18.00', '18.00', '0.00', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-08-29 09:27:05', '2025-09-09 16:12:43', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '18.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (702, 702, '2025-08-29', 'Viernes', '2:30 PM', '13.00', '13.00', '12.50', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-08-29 09:28:22', '2025-08-29 13:45:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '13.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (703, 703, '2025-08-29', 'Viernes', '3:00 PM', '5.00', '5.00', '4.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 5', 2, '2025-08-29 12:42:08', '2025-08-29 15:04:51', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '5.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (704, 704, '2025-09-01', 'Lunes', '9:00 AM', '10.00', '9.00', '8.75', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-01 09:16:13', '2025-09-01 09:42:09', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (705, 705, '2025-09-01', 'Lunes', '1:00 PM', '12.00', '12.00', '11.75', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', '4-6', 2, '2025-09-01 12:22:37', '2025-09-01 13:40:20', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (706, 706, '2025-09-01', 'Lunes', '10:00 AM', '40.00', '40.00', '40.00', '0', 'SAN ISIDRO,LIMA-LIMA -SAN ISIDRO', NULL, NULL, '2025-09-01 12:38:46', '2025-09-09 16:32:40', 'S', '15', '1501', '150131', 'LIMA', 'LIMA', 'SAN ISIDRO', 'SAN ISIDRO', '40.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (707, 707, '2025-09-01', 'Lunes', '4:00 PM', '5.00', '5.00', '4.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 5', 2, '2025-09-01 14:48:51', '2025-09-01 15:57:46', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '5.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (708, 708, '2025-09-02', 'Martes', '10:00 AM', '8.00', '8.00', '7.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-02 08:26:18', '2025-09-02 08:53:46', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (709, 709, '2025-09-02', 'Martes', '11:00 AM', '26.50', '26.50', '26.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-02 08:27:51', '2025-09-02 10:24:54', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '26.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (710, 710, '2025-09-02', 'Martes', '2:00 PM', '7', '7', '7', '0', 'SAN JUAN DE LURIGANCHO,LIMA-LIMA -SAN JUAN DE LURIGANCHO', NULL, 6, '2025-09-02 14:29:37', '2025-09-09 16:31:39', 'S', '15', '1501', '150132', 'LIMA', 'LIMA', 'SAN JUAN DE LURIGANCHO', 'SAN JUAN DE LURIGANCHO', '7', 'M3');
INSERT INTO `administracion_programacion` VALUES (711, 711, '2025-09-02', 'Martes', '4:00 PM', '6.00', '6.00', '5.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4', 2, '2025-09-02 15:14:24', '2025-09-02 16:47:20', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (712, 712, '2025-09-03', 'Miércoles', '2:30 PM', '13.00', '13.00', '12.50', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-03 10:38:11', '2025-09-03 14:24:23', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '13.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (713, 713, '2025-09-03', 'Miércoles', '1:30 PM', '12.00', '12.00', '11.75', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-03 11:19:12', '2025-09-03 14:09:13', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (714, 714, '2025-09-03', 'Miércoles', '4:00 PM', '6.00', '6.00', '5.75', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 5', NULL, '2025-09-03 16:15:44', '2025-09-03 16:40:56', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (715, 715, '2025-09-04', 'Jueves', '10:00 AM', '20.00', '20.00', '19.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-04 08:58:29', '2025-09-04 09:46:53', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (716, 716, '2025-09-04', 'Jueves', '9:30 AM', '95.00', '91.00', '89.50', '0.00', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'SLUMP 7', 2, '2025-09-04 09:21:48', '2025-09-09 16:10:19', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (717, 717, '2025-09-04', 'Jueves', '3:00 PM', '6.00', '5.50', '5.25', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP', 2, '2025-09-04 14:44:26', '2025-09-04 15:27:53', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (718, 718, '2025-09-04', 'Jueves', '2:30 PM', '6.00', '6.00', '6.00', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'SIN FLETE', 2, '2025-09-04 15:21:45', '2025-09-04 15:49:48', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (719, 719, '2025-09-05', 'Viernes', '10:30 AM', '20.50', '20.50', '20.00', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 2, '2025-09-04 17:40:50', '2025-09-05 10:15:44', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '20.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (720, 720, '2025-09-05', 'Viernes', '2:30 PM', '9.5', '9.5', '9.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-05 13:10:44', '2025-09-05 13:43:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '9.5', 'M3');
INSERT INTO `administracion_programacion` VALUES (721, 721, '2025-09-05', 'Viernes', '4:00 PM', '6.00', '5.50', '5.25', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4', 2, '2025-09-05 14:06:53', '2025-09-05 16:30:19', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (722, 722, '2025-09-06', 'Sábado', '8:00 AM', '6.00', '6.00', '5.75', '0', 'SAN ISIDRO,LIMA-LIMA -SAN ISIDRO', '267', 2, '2025-09-05 16:43:43', '2025-09-06 07:33:59', 'S', '15', '1501', '150131', 'LIMA', 'LIMA', 'SAN ISIDRO', 'SAN ISIDRO', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (723, 723, '2025-09-06', 'Sábado', '12:00 PM', '15.00', '15.00', '0.00', '-1.00', 'CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', '18 TUBERIAS ADC', 7, '2025-09-05 17:55:27', '2025-09-17 10:25:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (724, 724, '2025-09-06', 'Sábado', '11:00 AM', '17.50', '13.50', '13.25', '0', 'VITARTE,LIMA-LIMA -ATE', '250', 2, '2025-09-06 09:29:07', '2025-09-06 12:50:55', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'VITARTE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (725, 725, '2025-09-06', 'Sábado', '3:00 PM', '6.00', '4.00', '3.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4', 2, '2025-09-06 11:30:37', '2025-09-06 14:54:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (726, 726, '2025-09-08', 'Lunes', '12:00 PM', '7.00', '6.00', '5.75', '0', 'CERRO CAMOTE,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-06 14:32:22', '2025-09-08 12:54:26', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CERRO CAMOTE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (727, 727, '2025-09-08', 'Lunes', '1:00 PM', '15.00', '15.00', '14.75', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', 'FRECUENCIA: 02 HORAS', 2, '2025-09-06 14:34:22', '2025-09-08 14:15:31', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '15.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (728, 728, '2025-09-08', 'Lunes', '10:00 AM', '25.50', '25.50', '25.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-06 14:49:29', '2025-09-08 10:03:14', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '25.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (729, 729, '2025-09-08', 'Lunes', '11:00 AM', '8.00', '8.00', '7.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-06 14:50:23', '2025-09-08 10:22:48', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (730, 730, '2025-09-09', 'Martes', '2:30 PM', '10.00', '10.00', '9.75', '0', 'CALLE 9 C/CALLE 3 ,LIMA-LIMA -SAN BORJA', 'SLUMP 7', 7, '2025-09-09 12:51:36', '2025-09-09 13:46:25', 'S', '15', '1501', '150130', 'LIMA', 'LIMA', 'SAN BORJA', 'CALLE 9 C/CALLE 3', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (731, 731, '2025-09-09', 'Martes', '3:00 PM', '6.00', '5.00', '4.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-09 13:29:38', '2025-09-09 15:45:30', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (732, 732, '2025-09-10', 'Miércoles', '10:00 AM', '19.50', '19.50', '19.00', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-09 17:48:15', '2025-09-10 10:00:43', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '19.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (733, 733, '2025-09-10', 'Miércoles', '11:00 AM', '12.50', '12.50', '12.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-09 17:49:01', '2025-09-10 10:27:55', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '12.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (734, 734, '2025-09-10', 'Miércoles', '4:00 PM', '6.00', '5.00', '4.75', '0', 'HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 6 EN OBRA', 2, '2025-09-10 16:48:14', '2025-09-10 17:34:32', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (735, 735, '2025-09-11', 'Jueves', '10:00 AM', '20.00', '20.00', '19.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-11 08:37:25', '2025-09-11 09:49:03', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (736, 736, '2025-09-11', 'Jueves', '2:30 PM', '10.00', '10.00', '9.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-11 08:38:10', '2025-09-11 13:47:47', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (737, 737, '2025-09-11', 'Jueves', '4:00 PM', '6.00', '4.50', '4.25', '0', 'AV. LOS TUCANES  ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-11 14:00:32', '2025-09-11 17:36:02', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (738, 738, '2025-09-11', 'Jueves', '2:00 PM', '6.00', '5.00', '4.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-11 14:03:03', '2025-09-11 14:41:20', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (739, 739, '2025-09-11', 'Jueves', '4:00 PM', '10.50', '10.50', '10.25', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-11 14:55:30', '2025-09-11 16:31:17', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '10.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (740, 740, '2025-09-12', 'Viernes', '1:00 PM', '16.00', '16.00', '15.75', '0', 'CALLE 9 C/CALLE 3 ,LIMA-LIMA -SAN BORJA', 'SLUMP 7', 7, '2025-09-11 17:51:54', '2025-09-12 13:48:23', 'S', '15', '1501', '150130', 'LIMA', 'LIMA', 'SAN BORJA', 'CALLE 9 C/CALLE 3', '16.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (741, 741, '2025-09-12', 'Viernes', '10:30 AM', '25.00', '25.00', '24.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-11 17:54:11', '2025-09-12 10:02:19', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '25.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (742, 742, '2025-09-12', 'Viernes', '3:00 PM', '8.50', '8.50', '8.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 8', 6, '2025-09-11 17:55:19', '2025-09-12 14:47:59', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (743, 743, '2025-09-12', 'Viernes', '4:00 PM', '7.50', '7.50', '7.25', '0', 'AV. LOS TUCANES  ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 4, '2025-09-11 17:57:15', '2025-09-12 16:49:11', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (744, 744, '2025-09-12', 'Viernes', '4:00 PM', '12.00', '12.00', '11.75', '0', 'AV. LOS TUCANES  ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', NULL, '2025-09-11 17:57:54', '2025-09-12 16:32:50', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (745, 745, '2025-09-12', 'Viernes', '4:00 PM', NULL, NULL, '0.00', '0.00', 'AV. LOS TUCANES  ,LIMA-LIMA -LURIGANCHO', 'ANULAR PEDIDO / NO SE ATENDIO', NULL, '2025-09-11 17:58:54', '2025-09-12 16:02:10', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '6.00', NULL);
INSERT INTO `administracion_programacion` VALUES (746, 746, '2025-09-11', 'Jueves', '5:00 PM', '6.00', '1.5', '1.5', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-11 18:04:21', '2025-09-11 18:25:22', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (747, 747, '2025-09-12', 'Viernes', '8:00 AM', '24.00', '22.00', '20.75', '-1.00', 'CERRO CAMOTE ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', NULL, '2025-09-12 08:25:51', '2025-09-16 12:34:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CERRO CAMOTE', '0.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (748, 748, '2025-09-13', 'Sábado', '7:00 AM', '16.50', '16.50', '16.25', '0', 'CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', 'SLUMP 4', NULL, '2025-09-12 17:26:30', '2025-09-13 09:17:59', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '16.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (749, 749, '2025-09-13', 'Sábado', '8:00 AM', '7.00', '5.50', '5.25', '0', 'NIEVERIA ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', NULL, '2025-09-12 17:27:34', '2025-09-13 08:46:53', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (750, 750, '2025-09-13', 'Sábado', '9:00 AM', '8.00', '8.00', '7.75', '0', 'CONDOMINIO EL ENSUEÑO ,LIMA-HUAROCHIRÍ -RICARDO PALMA', 'SLUMP 4-6', NULL, '2025-09-12 17:30:21', '2025-09-13 07:30:16', 'S', '15', '1507', '150714', 'LIMA', 'HUAROCHIRÍ', 'RICARDO PALMA', 'CONDOMINIO EL ENSUEÑO', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (751, 751, '2025-09-13', 'Sábado', '11:00 AM', '20.00', '19.50', '19.25', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', '275/  FRECUENCIA DE HORA Y MEDIA', 7, '2025-09-12 18:34:27', '2025-09-13 11:30:55', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (752, 752, '2025-09-13', 'Sábado', '4:00 PM', '7.50', '7.50', '7.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 6, '2025-09-13 10:28:56', '2025-09-13 15:58:59', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (753, 753, '2025-09-13', 'Sábado', '4:00 PM', '8.00', '8.00', '7.75', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-13 10:29:50', '2025-09-13 16:41:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (754, 754, '2025-09-13', 'Sábado', '4:00 PM', '6.00', '4.50', '4.25', '0', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-13 13:08:38', '2025-09-13 16:55:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES S/N SANTA MARIA DE HUACHIPA', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (755, 755, '2025-09-15', 'Lunes', '9:30 AM', '20.00', '20.00', '19.50', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-15 08:38:28', '2025-09-15 10:22:06', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '20.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (756, 756, '2025-09-15', 'Lunes', '4:30 PM', '9.50', '9.50', '9.25', '0', 'AV. LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-15 08:39:12', '2025-09-15 16:55:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '9.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (757, 757, '2025-09-15', 'Lunes', '1:30 PM', '8.50', '8.50', '8.25', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-15 10:16:02', '2025-09-15 13:29:39', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (758, 758, '2025-09-15', 'Lunes', '4:00 PM', '7.50', '7.50', '7.25', '0', 'AV. LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 6, '2025-09-15 15:08:51', '2025-09-15 16:38:49', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV. LOS TUCANES', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (759, 759, '2025-09-16', 'Martes', '10:30 AM', '19.50', '19.50', '19', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-16 08:53:34', '2025-09-16 10:12:44', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '19.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (760, 760, '2025-09-16', 'Martes', '11:00 AM', '7.50', '7.50', '7.25', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-16 11:29:16', '2025-09-16 11:59:03', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (761, 761, '2025-09-16', 'Martes', '2:30 PM', '11.00', '11.00', '10.75', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 8', 6, '2025-09-16 13:07:24', '2025-09-16 13:35:58', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '11.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (762, 762, '2025-09-16', 'Martes', '4:00 PM', '14.00', '14.00', '13.75', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', NULL, 7, '2025-09-16 15:48:30', '2025-09-16 17:06:07', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '14.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (763, 763, '2025-09-16', 'Martes', '4:00 PM', '8.00', '8.00', '7.75', '0', 'AV LOS TUCANES ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 4, '2025-09-16 16:12:42', '2025-09-16 16:40:28', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (764, 764, '2025-09-16', 'Martes', '4:30 PM', '12.50', '12.50', '12.25', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-16 16:14:11', '2025-09-16 17:31:47', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '12.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (765, 765, '2025-09-17', 'Miércoles', '9:00 AM', '25.00', '23.00', '22.75', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-17 08:13:22', '2025-09-17 09:20:06', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (766, 766, '2025-09-17', 'Miércoles', '9:00 AM', '43.00', '41.00', '40.75', '0', 'VITARTE,LIMA-LIMA -ATE', 'SLUMP 6-8', NULL, '2025-09-17 09:14:44', '2025-09-17 13:11:22', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'VITARTE', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (767, 767, '2025-09-17', 'Miércoles', '12:00 PM', '7.50', '7.50', '7.25', '0', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-17 10:00:39', '2025-09-17 12:18:42', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'MZA. C LOTE. 5 ---- ASOC.HUERTA GRANJA', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (768, 768, '2025-09-17', 'Miércoles', '2:30 PM', '8.50', '8.50', '8.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6-8', 6, '2025-09-17 10:06:01', '2025-09-17 13:29:53', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (769, 769, '2025-09-17', 'Miércoles', '4:30 PM', '8.50', '8.50', '8.25', '0.00', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 4, '2025-09-17 16:09:49', '2025-09-19 10:42:59', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (770, 770, '2025-09-17', 'Miércoles', '4:30 PM', '13.00', '13.00', '13.00', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-17 16:10:42', '2025-09-17 17:04:26', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '13.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (771, 771, '2025-09-18', 'Jueves', '8:00 AM', '6.50', '6.50', '6.50', '0', 'HUAYCOLORO ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', NULL, '2025-09-18 08:11:01', '2025-09-18 08:33:01', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUAYCOLORO', '6.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (772, 772, '2025-09-18', 'Jueves', '10:30 AM', '25.00', '25.00', '24.50', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-18 08:22:15', '2025-09-18 09:48:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '25.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (773, 773, '2025-09-18', 'Jueves', '11:30 AM', '7.50', '7.50', '7.25', '0', 'JR. HUÁSCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 8', 6, '2025-09-18 08:23:31', '2025-09-18 10:11:13', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR. HUÁSCAR 2162', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (774, 774, '2025-09-18', 'Jueves', '11:00 AM', '7.50', '7.50', '7.25', '0', 'CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-18 10:26:20', '2025-09-18 12:22:01', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (775, 775, '2025-09-18', 'Jueves', '4:00 PM', '9.00', '9.00', '8.75', '0', 'PETRAMAS,LIMA-LIMA -LURIGANCHO', 'SLUMP DE \"4-6\"', 2, '2025-09-18 10:28:26', '2025-09-18 16:25:36', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'PETRAMAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (776, 776, '2025-09-18', 'Jueves', '12:30 PM', '7.00', '7.00', '7', '0', 'HUAYCOLORO ,LIMA-LIMA -LURIGANCHO', 'SLUMP 4-6', NULL, '2025-09-18 13:05:09', '2025-09-18 13:28:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'HUAYCOLORO', '7.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (777, 777, '2025-09-18', 'Jueves', '3:00 PM', '10.50', '10.50', '10.25', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', 'SE APLICA EL MEDIO CUBO QUE SE DEBE DEL 13/09, POR CONFUSION DE GUIA', 7, '2025-09-18 13:45:11', '2025-09-18 14:53:36', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '10.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (778, 778, '2025-09-18', 'Jueves', '4:30 PM', '12.00', '12.00', '11.75', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 6, '2025-09-18 15:41:36', '2025-09-18 16:49:56', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (779, 779, '2025-09-19', 'Viernes', '10:00 AM', '7.50', '7.50', '7.25', '0', 'CAJAMARQUILLA,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-19 08:53:07', '2025-09-19 10:46:49', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLA', '7.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (780, 780, '2025-09-19', 'Viernes', '4:30 PM', '21.00', '21.00', '20.75', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 6, '2025-09-19 15:52:24', '2025-09-19 17:31:41', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '21.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (781, 781, '2025-09-20', 'Sábado', '11:00 AM', '8.00', '8.00', '7.75', '0', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', NULL, 2, '2025-09-20 09:12:59', '2025-09-20 10:35:49', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '8.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (782, 782, '2025-09-20', 'Sábado', '11:00 AM', '8.00', '8.00', '7.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', 'SLUMP 3-4', 2, '2025-09-20 11:25:35', '2025-09-20 11:58:50', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (783, 783, '2025-09-20', 'Sábado', '11:00 PM', '8.50', '8.00', '7.75', '0', 'CAJAMARQUILLAS,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-20 12:52:05', '2025-09-20 13:24:05', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CAJAMARQUILLAS', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (784, 784, '2025-09-20', 'Sábado', '3:00 PM', '11.00', '11.00', '10.75', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 6, '2025-09-20 13:28:33', '2025-09-20 15:25:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '11.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (785, 785, '2025-09-20', 'Sábado', '3:00 PM', '6.00', '4.00', '4.00', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', 2, '2025-09-20 13:29:41', '2025-09-20 15:37:58', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (786, 786, '2025-09-21', 'Domingo', '8:00 AM', '17.00', '16.00', '15.75', '0', 'CHORRILLOS ,LIMA-LIMA -CHORRILLOS', NULL, 2, '2025-09-20 15:03:27', '2025-09-21 08:18:46', 'S', '15', '1501', '150108', 'LIMA', 'LIMA', 'CHORRILLOS', 'CHORRILLOS', '17.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (787, 787, '2025-09-22', 'Lunes', '10:30 AM', '20.50', '20.50', '20.25', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-22 09:12:39', '2025-09-22 10:17:50', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '20.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (788, 788, '2025-09-22', 'Lunes', '11:00 AM', '13.00', '13.00', '12.50', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-22 09:13:28', '2025-09-22 11:08:45', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '13.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (789, 789, '2025-09-22', 'Lunes', '10:00 AM', '9.00', '7.50', '7.25', '0', 'CARAPONGO,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-22 09:44:44', '2025-09-22 10:39:33', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CARAPONGO', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (790, 790, '2025-09-22', 'Lunes', '4:30 PM', '6.50', '6.50', '6.25', '0.00', 'JESUS MARIA,LIMA-LIMA -JESÚS MARÍA', NULL, 2, '2025-09-22 15:33:35', '2025-09-22 16:03:12', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JESUS MARIA', '6.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (791, 791, '2025-09-22', 'Lunes', '4:30 PM', '6.00', '4.50', '4.25', '0.00', 'AV LOS TUCANES ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6', NULL, '2025-09-22 16:23:14', '2025-09-22 16:52:41', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (792, 792, '2025-09-23', 'Martes', '11:00 AM', '21.50', '21.50', '21.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-23 09:29:37', '2025-09-23 10:28:21', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '21.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (793, 793, '2025-09-23', 'Martes', '4:30 PM', '6.50', '6.50', '6.25', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-09-23 15:43:54', '2025-09-23 17:03:09', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '6.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (794, 794, '2025-09-23', 'Martes', '4:30 PM', '9.00', '9.00', '9.00', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-23 15:44:57', '2025-09-23 17:44:06', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '9.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (795, 795, '2025-09-23', 'Martes', '4:30 PM', '2.50', '2.50', '2.50', '0.00', 'AV LOS TUCANES ,LIMA-LIMA -LURIGANCHO', 'SLUMP 6 - C/FIBRA METALICA', NULL, '2025-09-23 16:58:39', '2025-09-23 17:23:53', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '2.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (796, 796, '2025-09-25', 'Jueves', '1:00 PM', '41.50', '41.50', '41.00', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 4-6', 6, '2025-09-25 09:29:53', '2025-09-25 13:34:46', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '41.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (797, 797, '2025-09-25', 'Jueves', '3:00 PM', '10.00', '10.00', '10.00', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', NULL, 6, '2025-09-25 09:30:41', '2025-09-25 15:24:11', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (798, 798, '2025-09-25', 'Jueves', '4:00 PM', '1.00', '1.00', '1', '0', 'AV LOS TUCANES ,LIMA-LIMA -LURIGANCHO', NULL, 7, '2025-09-25 17:01:52', '2025-09-25 17:21:12', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '1.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (799, 799, '2025-09-25', 'Jueves', '4:00 PM', '2.50', '2.50', '2.5', '0', 'AV LOS TUCANES ,LIMA-LIMA -LURIGANCHO', NULL, 7, '2025-09-25 17:27:49', '2025-09-25 17:35:54', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '2.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (800, 800, '2025-09-26', 'Viernes', '9:30 AM', '6.50', '6.50', '6.25', '0', 'AV. JAVIER PRADO ESTE ,LIMA-LIMA -LINCE', 'SLUMP 6', NULL, '2025-09-26 08:52:02', '2025-09-26 09:22:08', 'S', '15', '1501', '150116', 'LIMA', 'LIMA', 'LINCE', 'AV. JAVIER PRADO ESTE', '6.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (801, 801, '2025-09-26', 'Viernes', '2:30 PM', '11.00', '11.00', '10.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-09-26 13:04:27', '2025-09-26 13:28:06', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '11.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (802, 802, '2025-09-27', 'Sábado', '7:30 AM', '19.50', '19.50', '19.50', '1', 'LA MOLINA,LIMA-LIMA -LA MOLINA', NULL, 6, '2025-09-26 14:51:09', '2025-10-03 11:35:28', 'S', '15', '1501', '150114', 'LIMA', 'LIMA', 'LA MOLINA', 'LA MOLINA', NULL, 'M3');
INSERT INTO `administracion_programacion` VALUES (803, 803, '2025-09-27', 'Sábado', '10:30 AM', '8.50', '8.50', '8.50', '0', 'AV LO QUECHUAS,LIMA-LIMA -ATE', NULL, 6, '2025-09-29 10:54:51', '2025-10-03 11:35:04', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'AV LO QUECHUAS', '8.50', 'M3');
INSERT INTO `administracion_programacion` VALUES (804, 804, '2025-09-29', 'Lunes', '1:00 PM', '26.00', '26.00', '25.00', '0', 'JR.HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', 'SLUMP 6', 6, '2025-09-29 12:03:32', '2025-09-29 13:01:39', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR.HUASCAR 2162', '26.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (805, 805, '2025-09-26', 'Viernes', '2:00 PM', '18.00', '18.00', '18.00', '0', 'ZARATE ,LIMA-LIMA -SAN JUAN DE LURIGANCHO', 'ADICIONAL 5 TUBERIAS S/20', 7, '2025-09-29 12:05:26', '2025-10-03 11:36:15', 'S', '15', '1501', '150132', 'LIMA', 'LIMA', 'SAN JUAN DE LURIGANCHO', 'ZARATE', '18.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (806, 806, '2025-09-29', 'Lunes', '12:00 PM', '12.00', '12.00', '12.00', '0', 'HUAYCAN ,LIMA-LIMA -ATE', 'ING BRYAN / 550', 7, '2025-09-29 15:24:16', '2025-10-03 11:36:39', 'S', '15', '1501', '150103', 'LIMA', 'LIMA', 'ATE', 'HUAYCAN', '12.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (807, 807, '2025-09-30', 'Martes', '8:00 AM', '28.00', '28.00', '27.75', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', 'X M3 230 , ESTC 750', 7, '2025-09-30 08:16:06', '2025-09-30 09:16:34', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (808, 808, '2025-09-30', 'Martes', '1:00 PM', '10.00', '10.00', '10.00', '0', 'CERRO CAMOTE,LIMA-LIMA -LURIGANCHO', '550 - CANCELADO', 6, '2025-09-30 10:43:03', '2025-10-03 11:36:56', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'CERRO CAMOTE', '10.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (809, 809, '2025-09-30', 'Martes', '12:00 PM', '2.5', '2.5', '2.5', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', 'FLETE 100', NULL, '2025-09-30 12:38:28', '2025-09-30 12:50:52', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', '0', 'M3');
INSERT INTO `administracion_programacion` VALUES (810, 810, '2025-09-30', 'Martes', '2:00 PM', '18.50', '18.50', '18.50', '0', 'NIEVERIA,LIMA-LIMA -LURIGANCHO', '550-CANCELADO EFECTIVO 30/09', 6, '2025-09-30 15:31:52', '2025-10-03 11:37:17', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'NIEVERIA', NULL, 'M3');
INSERT INTO `administracion_programacion` VALUES (811, 811, '2025-09-30', 'Martes', '4:00 PM', '6.00', '3.00', '3.00', '0', 'AV LOS TUCANES,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-09-30 15:34:07', '2025-09-30 16:41:31', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'AV LOS TUCANES', '6.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (818, 818, '2025-10-01', 'Miércoles', '11:00 PM', '16.00', '16.00', '15.75', '0', 'JR HUASCAR 2162,LIMA-LIMA -JESÚS MARÍA', NULL, 6, '2025-10-01 11:37:37', '2025-10-01 15:23:17', 'S', '15', '1501', '150113', 'LIMA', 'LIMA', 'JESÚS MARÍA', 'JR HUASCAR 2162', '16.00', 'M3');
INSERT INTO `administracion_programacion` VALUES (819, 819, '2025-10-01', 'Miércoles', '9:00 PM', '13.00', '10.00', '9.75', '0', 'LAS AMERICAS,LIMA-LIMA -LURIGANCHO', NULL, 2, '2025-10-01 11:40:07', '2025-10-01 15:25:57', 'S', '15', '1501', '150118', 'LIMA', 'LIMA', 'LURIGANCHO', 'LAS AMERICAS', '13.00', 'M3');

-- ----------------------------
-- Table structure for administracion_proveedor
-- ----------------------------
DROP TABLE IF EXISTS `administracion_proveedor`;
CREATE TABLE `administracion_proveedor`  (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `nombre_empresa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ruc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contacto_principal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tipo_proveedor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `terminos_pago` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cuenta_bancaria` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sitio_web` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `notas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `fecha_inicio` date NULL DEFAULT NULL,
  `calificacion` int NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'S=Activo, N=Inactivo',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_proveedor`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_proveedor
-- ----------------------------
INSERT INTO `administracion_proveedor` VALUES (3, 'MINORISTA VARIOS', '11111111111', NULL, NULL, NULL, 'MINORISTA', 'INSUMOS', NULL, NULL, NULL, NULL, '2025-07-01', NULL, 'S', '2025-07-01 23:13:17', '2025-07-01 23:13:17');
INSERT INTO `administracion_proveedor` VALUES (4, 'ABASTECEDOR DE AGUA NIEVERIA', '11111111112', 'NIEVERIA - COSTADO DEL GRIFO GODO', NULL, NULL, NULL, 'MATERIALES', '1', NULL, NULL, NULL, '2025-07-02', 1, 'S', '2025-07-02 15:26:34', '2025-07-02 15:26:34');
INSERT INTO `administracion_proveedor` VALUES (5, 'RUTAS DE LIMA SAC', '20550372640', 'CAR.PANAMERICANA SUR KM. 19.65 NRO. S/N LIMA - LIMA - VILLA EL SALVADOR', NULL, NULL, NULL, 'INSUMOS', '1', NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 15:33:13', '2025-07-02 15:33:13');
INSERT INTO `administracion_proveedor` VALUES (6, 'AC-PERSONAL', '00000000000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 15:46:44', '2025-07-02 15:46:44');
INSERT INTO `administracion_proveedor` VALUES (7, 'W & A SERVICIOS Y REPUESTOS SAC', '20609772612', 'CAL. ANCASH NRO. 304 DPTO. 304 RES. CONDOMINIO LOS FRESNOS', NULL, NULL, NULL, 'MATERIALES', '1', NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:40:46', '2025-07-02 16:41:52');
INSERT INTO `administracion_proveedor` VALUES (8, 'HOMCENTERS PERUANOS S.A', '20536557858', 'AV. AVIACION NRO. 2405 (PISO 5) LIMA - LIMA - SAN BORJA', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:42:46', '2025-07-02 18:29:15');
INSERT INTO `administracion_proveedor` VALUES (9, 'LIMA EXPRESA SAC', '20523621212', 'AV. EL DERBY NRO. 250 (URB.EL DERBY DE MONTERRICO-OFC.1701 1801) LIMA - LIMA - SANTIAGO DE SURCO', NULL, NULL, NULL, 'INSUMOS', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:43:31', '2025-07-02 18:29:36');
INSERT INTO `administracion_proveedor` VALUES (10, 'LUBRIMAQ EIRL', '20547041942', 'AV. LAS TORRES SUB LOTE 2-A MZA. L DPTO. P1 C.P. MENOR SANTA MARÍA DE HUACHIPA LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:44:30', '2025-07-02 18:35:35');
INSERT INTO `administracion_proveedor` VALUES (11, 'INVER-FERR ROSITA SAC', '20555497963', 'AV. COLECTORA LOTE. 7AI APV. HAB PRE URB NIEVERIA II (PARCELA 28) LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:46:07', '2025-07-02 18:30:54');
INSERT INTO `administracion_proveedor` VALUES (12, 'MECANISMOS E HIDRAULICAS SAC', '20563383811', 'AV. CAJAMARQUILLA MZA. N1 LOTE. 01A AS PROP PORTALES NIEVERIA LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:47:53', '2025-07-02 18:31:13');
INSERT INTO `administracion_proveedor` VALUES (13, 'MEDINA MELO RONALD', '10446454299', NULL, NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:48:38', '2025-07-02 16:48:38');
INSERT INTO `administracion_proveedor` VALUES (14, 'FERRETERIA DISTRIBUIDORA Y MATIZADOS HUAÑEC', '20515699628', 'AV. LAS TORRES MZA. A-13 LOTE. 1 U.V. NIEVERIA (A 1 CDR. DE FABRICA GLORIA) LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:49:33', '2025-07-02 18:31:54');
INSERT INTO `administracion_proveedor` VALUES (15, 'KMK HIDRAULICA Y SERVICIOS SA', '20432420834', 'AV. RAMON CARCAMO NRO. 726 URB. RESIDENCIAL LIMA INDUSTRIAL LIMA - LIMA - LIMA', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-02', NULL, 'S', '2025-07-02 16:50:30', '2025-07-02 18:32:11');
INSERT INTO `administracion_proveedor` VALUES (16, 'TAI LOY S.A', '20100049181', 'JR. MARIANO ODICIO NRO. 153 URB. MIRAFLORES (MZ L, LOTE 144, SUB LOTE A) LIMA - LIMA - SURQUILLO', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-03', NULL, 'S', '2025-07-03 14:08:56', '2025-07-03 14:08:56');
INSERT INTO `administracion_proveedor` VALUES (17, 'ASOCIACION DE PROPIETARIOS DEL SISTEMA DE AGUA POTABLE SANEAMIENTO Y MEDIO AMBIENTE DE NIEVERIA', '20507048669', 'MZA. B LOTE. 6 ASOC.DE POBL.LOS JARDINES (COSTADO DEL ESTADIO DE NIEVERIA) LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-03', NULL, 'S', '2025-07-03 20:22:05', '2025-07-03 20:22:05');
INSERT INTO `administracion_proveedor` VALUES (18, 'PAPELETAS', '11111111113', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-04', NULL, 'S', '2025-07-04 12:47:46', '2025-07-04 12:48:12');
INSERT INTO `administracion_proveedor` VALUES (19, 'FISCALIZACION', '11111111114', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-04', NULL, 'S', '2025-07-04 14:57:41', '2025-07-04 14:57:41');
INSERT INTO `administracion_proveedor` VALUES (20, 'ARENA NIEVERIA', '11111111115', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-04', NULL, 'S', '2025-07-04 15:36:43', '2025-07-04 15:36:56');
INSERT INTO `administracion_proveedor` VALUES (21, 'BOMBEOS J&V', '11111111116', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-04', NULL, 'S', '2025-07-04 15:46:25', '2025-07-04 15:46:32');
INSERT INTO `administracion_proveedor` VALUES (22, 'ARENA VALLECITO', '11111111117', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-04', NULL, 'S', '2025-07-04 15:52:01', '2025-07-08 14:02:14');
INSERT INTO `administracion_proveedor` VALUES (23, 'NOR BUILDING S.A.C', '20534156287', 'AV. CARLOS IZAGUIRRE CENTRO C S/N URB. CAJABAMBA INT. 66  LOS OLIVOS - LIMA - LIMA', NULL, NULL, NULL, 'MATERIALES', NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 13:48:24', '2025-07-08 13:48:24');
INSERT INTO `administracion_proveedor` VALUES (24, 'TRAMITES VARIOS ADMINISTRATIVOS', '11111111119', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 14:02:05', '2025-07-08 19:52:44');
INSERT INTO `administracion_proveedor` VALUES (25, 'ESTACION DE SERVICIOS EL GODO SAC', '20602629709', 'LOTE. 3B EX FUNDO NIEVERIA LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 15:07:04', '2025-07-08 15:07:04');
INSERT INTO `administracion_proveedor` VALUES (26, 'LUZ DEL SUR S.A.A', '20331898008', 'AV. CANAVAL Y MOREYRA NRO. 380 LIMA - LIMA - SAN ISIDRO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 16:42:11', '2025-07-08 16:42:29');
INSERT INTO `administracion_proveedor` VALUES (27, 'SERVICIO DE PARQUES DE LIMA SERPAR - LIMA', '20145913544', 'AV. LOS PARQUES NRO. 251 FND. VALDIVIEZO (FRENTE A URB. VALDIVIEZO) LIMA - LIMA - ATE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 16:49:48', '2025-07-08 16:49:48');
INSERT INTO `administracion_proveedor` VALUES (28, 'TABOADA HUAYLLANI AYDEE', '10400228308', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 19:10:58', '2025-07-08 19:10:58');
INSERT INTO `administracion_proveedor` VALUES (29, 'CARAPO SALAZAR WENDY SHEYLA', '10774646073', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-08', NULL, 'S', '2025-07-08 19:12:10', '2025-07-08 19:12:10');
INSERT INTO `administracion_proveedor` VALUES (30, 'DESARROLLO VIAL DE LOS ANDES S.A.C.', '20537464543', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-09', NULL, 'S', '2025-07-09 15:04:49', '2025-07-09 15:04:49');
INSERT INTO `administracion_proveedor` VALUES (31, 'UNION DE CONCRETERAS SA', '20297543653', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-12', NULL, 'S', '2025-07-12 14:25:56', '2025-07-12 14:25:56');
INSERT INTO `administracion_proveedor` VALUES (32, 'FERRETERIA Y MATIZADOS CAJAMARQUILLA EIRL', '20602995110', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-14', NULL, 'S', '2025-07-14 13:29:41', '2025-07-14 13:29:41');
INSERT INTO `administracion_proveedor` VALUES (33, 'INVERSIONES PURITA CALIDAD SRL', '20514993956', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-14', NULL, 'S', '2025-07-14 13:48:30', '2025-07-14 13:48:30');
INSERT INTO `administracion_proveedor` VALUES (34, 'ORION LABORATORIOS EIRL', '20493967089', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-14', NULL, 'S', '2025-07-14 14:58:48', '2025-07-14 14:58:48');
INSERT INTO `administracion_proveedor` VALUES (35, 'REPUESTOS CARLITOS SAC', '20602986242', 'AV. LAS TORRES MZA. K LOTE. 01 URB. LA CAPITANA (AL COSTADO FABRICA LECHE GLORIA) LIMA - LIMA - LURIGANCHO', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-18', NULL, 'S', '2025-07-18 14:04:11', '2025-07-18 14:04:11');
INSERT INTO `administracion_proveedor` VALUES (36, 'FERRETERIA MODERNA', '10200698725', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-18', NULL, 'S', '2025-07-18 14:57:45', '2025-07-18 14:57:45');
INSERT INTO `administracion_proveedor` VALUES (37, 'GRUPO J&L SAC', '20606497165', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-25', NULL, 'S', '2025-07-25 17:08:24', '2025-07-25 17:08:24');
INSERT INTO `administracion_proveedor` VALUES (38, 'DEL VALLE ROJAS JULIETTE MARIE', '10726140257', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-25', NULL, 'S', '2025-07-25 17:11:41', '2025-07-25 17:11:41');
INSERT INTO `administracion_proveedor` VALUES (39, 'FERRETERIA LUCIANITA', '11111111118', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-26', NULL, 'S', '2025-07-26 14:00:18', '2025-07-26 14:00:49');
INSERT INTO `administracion_proveedor` VALUES (40, 'FERRETERIA Y MATIZADOS ANITA EIRL', '20611935821', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-30', NULL, 'S', '2025-07-30 14:41:23', '2025-07-30 14:41:23');
INSERT INTO `administracion_proveedor` VALUES (41, 'GRIFO SAN IGNACIO SAC', '20100075858', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-08-04', NULL, 'S', '2025-08-04 13:30:40', '2025-08-04 13:30:40');

-- ----------------------------
-- Table structure for administracion_vehiculo
-- ----------------------------
DROP TABLE IF EXISTS `administracion_vehiculo`;
CREATE TABLE `administracion_vehiculo`  (
  `id_vehiculo` int NOT NULL AUTO_INCREMENT,
  `placa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `mixer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_vehiculo`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_vehiculo
-- ----------------------------
INSERT INTO `administracion_vehiculo` VALUES (1, 'BXW-930', '2025-04-09 17:58:31', '2025-08-04 17:27:46', 'S', '110');
INSERT INTO `administracion_vehiculo` VALUES (2, 'BJH-747', '2025-04-09 18:07:33', '2025-08-04 17:27:39', 'S', '109');
INSERT INTO `administracion_vehiculo` VALUES (3, 'BSX-719', '2025-07-08 15:57:08', '2025-08-04 17:27:32', 'S', '108');
INSERT INTO `administracion_vehiculo` VALUES (4, 'BAL-751', '2025-07-08 15:57:49', '2025-08-04 17:27:26', 'S', '107');
INSERT INTO `administracion_vehiculo` VALUES (5, 'BLV-905', '2025-07-08 15:57:58', '2025-08-04 17:27:17', 'S', '106');
INSERT INTO `administracion_vehiculo` VALUES (6, 'BJG-735', '2025-07-08 15:58:08', '2025-08-04 17:27:07', 'S', '105');
INSERT INTO `administracion_vehiculo` VALUES (7, 'BJK-914', '2025-07-08 15:58:18', '2025-08-04 17:26:58', 'S', '104');
INSERT INTO `administracion_vehiculo` VALUES (8, 'B8W-859', '2025-07-08 15:58:31', '2025-08-04 17:26:50', 'S', '102');
INSERT INTO `administracion_vehiculo` VALUES (9, 'CCE-943', '2025-07-08 15:58:44', '2025-08-04 17:26:40', 'S', '111');
INSERT INTO `administracion_vehiculo` VALUES (10, 'BYN-757', '2025-07-08 15:58:54', '2025-08-04 17:26:27', 'S', '103');
INSERT INTO `administracion_vehiculo` VALUES (11, 'Z7F-940', '2025-07-08 15:59:05', '2025-08-04 17:26:18', 'S', '101');

-- ----------------------------
-- Table structure for administracion_vendedor
-- ----------------------------
DROP TABLE IF EXISTS `administracion_vendedor`;
CREATE TABLE `administracion_vendedor`  (
  `id_vendedor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Tipo Vededor: \r\nE: Externo \r\nD: Directo',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_vendedor`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of administracion_vendedor
-- ----------------------------
INSERT INTO `administracion_vendedor` VALUES (1, 'TERESA MUÑOZ BARRUETO', '2025-03-18 15:11:37', '2025-03-18 15:11:37', 'S');
INSERT INTO `administracion_vendedor` VALUES (2, 'GEMA GERRA SAJAMI', '2025-03-18 15:12:04', '2025-03-18 15:12:04', 'S');
INSERT INTO `administracion_vendedor` VALUES (3, 'KELLY ROJAS FERNANDEZ', '2025-03-18 15:12:23', '2025-03-18 15:12:23', 'S');
INSERT INTO `administracion_vendedor` VALUES (4, 'CARLOS QUISPE ZACARIAS', '2025-03-18 15:14:05', '2025-03-18 15:14:05', 'S');
INSERT INTO `administracion_vendedor` VALUES (5, 'NATALY', '2025-03-21 21:16:17', '2025-03-21 21:16:17', 'S');
INSERT INTO `administracion_vendedor` VALUES (6, 'HENRY HUAYTA', '2025-03-25 20:14:21', '2025-03-25 20:14:21', 'S');
INSERT INTO `administracion_vendedor` VALUES (7, 'RODOLFO', '2025-03-31 12:38:03', '2025-03-31 12:38:03', 'S');
INSERT INTO `administracion_vendedor` VALUES (8, 'VILMA', '2025-06-10 12:44:54', '2025-06-10 12:44:54', 'S');
INSERT INTO `administracion_vendedor` VALUES (9, 'ELIZABETH', '2025-06-25 21:24:47', '2025-06-25 21:24:47', 'S');
INSERT INTO `administracion_vendedor` VALUES (10, 'TEDODORO JIMENEZ', '2025-07-02 18:40:38', '2025-07-02 18:40:38', 'S');

-- ----------------------------
-- Table structure for auditoria_accion
-- ----------------------------
DROP TABLE IF EXISTS `auditoria_accion`;
CREATE TABLE `auditoria_accion`  (
  `id_accion` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_accion`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auditoria_accion
-- ----------------------------

-- ----------------------------
-- Table structure for auditoria_seguridad
-- ----------------------------
DROP TABLE IF EXISTS `auditoria_seguridad`;
CREATE TABLE `auditoria_seguridad`  (
  `id_seguridad` int NOT NULL AUTO_INCREMENT,
  `id_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_accion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `ip_conexion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mac_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_seguridad`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of auditoria_seguridad
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_banners
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_banners`;
CREATE TABLE `ecommerce_banners`  (
  `id_banner` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `subtitulo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `imagen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `imagen_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `enlace` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tipo_enlace` enum('producto','categoria','url','ninguno') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ninguno',
  `id_enlace` int NULL DEFAULT NULL COMMENT 'ID del producto o categoría',
  `color_fondo` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `color_texto` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `posicion` enum('home','categoria','checkout') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'home',
  `orden` int NULL DEFAULT 0,
  `fecha_inicio` date NULL DEFAULT NULL,
  `fecha_fin` date NULL DEFAULT NULL,
  `clicks` int NULL DEFAULT 0,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_banner`) USING BTREE,
  INDEX `idx_posicion`(`posicion` ASC) USING BTREE,
  INDEX `idx_activo`(`activo` ASC) USING BTREE,
  INDEX `idx_fechas`(`fecha_inicio` ASC, `fecha_fin` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_banners
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_carrito_items
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_carrito_items`;
CREATE TABLE `ecommerce_carrito_items`  (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `id_carrito` int NOT NULL,
  `id_producto` int NOT NULL,
  `id_variante` int NULL DEFAULT NULL,
  `cantidad` int NOT NULL DEFAULT 1,
  `precio_unitario` decimal(10, 2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_item`) USING BTREE,
  UNIQUE INDEX `uk_carrito_producto`(`id_carrito` ASC, `id_producto` ASC, `id_variante` ASC) USING BTREE,
  INDEX `id_producto`(`id_producto` ASC) USING BTREE,
  INDEX `id_variante`(`id_variante` ASC) USING BTREE,
  INDEX `idx_carrito`(`id_carrito` ASC) USING BTREE,
  CONSTRAINT `ecommerce_carrito_items_ibfk_1` FOREIGN KEY (`id_carrito`) REFERENCES `ecommerce_carritos_olddddddd` (`id_carrito`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_carrito_items_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_carrito_items_ibfk_3` FOREIGN KEY (`id_variante`) REFERENCES `ecommerce_producto_variantes` (`id_variante`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_carrito_items
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_carritos_olddddddd
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_carritos_olddddddd`;
CREATE TABLE `ecommerce_carritos_olddddddd`  (
  `id_carrito` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NULL DEFAULT NULL,
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Para usuarios no logueados',
  `codigo_cupon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `descuento` decimal(10, 2) NULL DEFAULT 0.00,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_carrito`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_carritos_olddddddd
-- ----------------------------
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (1, NULL, 'ki8P9VyKWSY9aM5ia8xQxqj1mdGDbeSgPrIRNBqn', NULL, 0.00, '2026-01-25 12:37:21', '2026-01-25 12:37:21');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (2, NULL, 'TCmHefBpVbn8Aw0kmDFF0Qn2vP0wHLbzqgWJy9y6', NULL, 0.00, '2026-01-25 12:45:32', '2026-01-25 12:45:32');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (3, NULL, 'HZRpXaWSrNpea6Rfx7Up2ajKtFxIxhg0d1GaaD8B', NULL, 0.00, '2026-01-25 12:45:36', '2026-01-25 12:45:36');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (4, NULL, '4o6BwDfMQh3Ekrwzgz8EMRrtWXccKv2IrZdGFmKv', NULL, 0.00, '2026-01-25 12:49:40', '2026-01-25 12:49:40');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (7, NULL, 'HR8LAvzFa546D1VAwdt7RjeOodtBz0iI0o1m8yWM', NULL, 0.00, '2026-01-25 12:51:39', '2026-01-25 12:51:39');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (12, NULL, 'hfpHDllyjoMT7Sq1XMGorbkrkb7iRPHbOSwxwMPs', NULL, 0.00, '2026-01-25 12:58:20', '2026-01-25 12:58:20');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (13, 1, NULL, NULL, 0.00, '2026-01-25 12:58:37', '2026-01-25 12:58:37');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (14, NULL, 'Ac6hvnMkdDxnVFvGDuUdL41TKuVRHghBWROedgFB', NULL, 0.00, '2026-01-25 13:03:06', '2026-01-25 13:03:06');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (15, NULL, 'L3wib1yPzte0N2b0WaweaEufyXMcvq7O4lcqoDXK', NULL, 0.00, '2026-01-25 13:03:07', '2026-01-25 13:03:07');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (16, NULL, 'hahxE8SuVENLIuMaBEF227MkqsDqXsnmBBIgvkcJ', NULL, 0.00, '2026-01-25 13:09:14', '2026-01-25 13:09:14');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (17, NULL, 'wenyuRxBvE991MxQE9mcEJYm7f7IIyNbvZREldnm', NULL, 0.00, '2026-01-25 13:09:16', '2026-01-25 13:09:16');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (18, NULL, 'UVNY7zphkhFUYpQNRsAUr4JNduNaJaUV9243rhkj', NULL, 0.00, '2026-01-25 13:21:54', '2026-01-25 13:21:54');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (19, NULL, 'a60aomJNpSrCJkdesF6gGArexzhZ1WaIazpDNqGD', NULL, 0.00, '2026-01-25 13:21:56', '2026-01-25 13:21:56');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (20, NULL, 'taJgogVH414loq0r7f9Tk7RYsPasFiVFxSdZBhOI', NULL, 0.00, '2026-01-25 13:23:30', '2026-01-25 13:23:30');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (21, NULL, 'bwyRDUMRoevtfMKWDpEw0zAS5L0KmCEMehHBzGEm', NULL, 0.00, '2026-01-25 13:23:31', '2026-01-25 13:23:31');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (22, NULL, '0s3YGWlc0HO6yvJoraZ4hhQBkeOFwXw0TnY2w45A', NULL, 0.00, '2026-01-25 13:32:09', '2026-01-25 13:32:09');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (23, NULL, 'itDJXPi9xAIlEQK2H2o9trhchGVe90ZKCAGwn1DZ', NULL, 0.00, '2026-01-25 13:32:10', '2026-01-25 13:32:10');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (24, NULL, 'Sg4uqq3Ml7wDX2hRWTw2fNr08YaR3tTEmVbOltnX', NULL, 0.00, '2026-01-25 13:33:31', '2026-01-25 13:33:31');
INSERT INTO `ecommerce_carritos_olddddddd` VALUES (25, NULL, 'nSsNwn2x8LE6AMfP0q2d5FYr6lCVmEfDzqVLOaTr', NULL, 0.00, '2026-01-25 13:33:32', '2026-01-25 13:33:32');

-- ----------------------------
-- Table structure for ecommerce_categorias
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_categorias`;
CREATE TABLE `ecommerce_categorias`  (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `imagen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `id_categoria_padre` int NULL DEFAULT NULL,
  `orden` int NULL DEFAULT 0,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_categoria`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_categorias
-- ----------------------------
INSERT INTO `ecommerce_categorias` VALUES (1, 'Bebidas', 'bebidas', 'Gaseosas, aguas, jugos, energizantes y bebidas alcohólicas', 'bebidas.jpg', NULL, 10, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (2, 'Abarrotes', 'abarrotes', 'Productos secos de despensa: arroces, aceites, azúcar, fideos, etc.', 'abarrotes.jpg', NULL, 20, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (3, 'Snacks y Confitería', 'snacks-confiteria', 'Papas fritas, chocolates, galletas, dulces y golosinas', 'snacks.jpg', NULL, 30, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (4, 'Lácteos y Refrigerados', 'lacteos-refrigerados', 'Leches, yogures, quesos, mantequillas y otros refrigerados', 'lacteos.jpg', NULL, 40, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (5, 'Limpieza y Hogar', 'limpieza-hogar', 'Detergentes, jabones, papel higiénico, limpiadores, etc.', 'limpieza.jpg', NULL, 50, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (6, 'Cuidado Personal e Higiene', 'cuidado-personal', 'Shampoo, jabones, pastas dentales, desodorantes, pañales', 'cuidado-personal.jpg', NULL, 60, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (7, 'Perecibles', 'perecibles', 'Frutas, verduras, carnes, embutidos y productos frescos', 'perecibles.jpg', NULL, 70, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (8, 'Gaseosas', 'gaseosas', NULL, 'gaseosas.jpg', 1, 11, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (9, 'Aguas y Bebidas Sin Gas', 'aguas-sin-gas', NULL, 'aguas.jpg', 1, 12, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (10, 'Cereales y Granos', 'cereales-granos', 'Arroz, menestras, avena, etc.', 'granos.jpg', 2, 21, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (11, 'Aceites y Grasas', 'aceites-grasas', NULL, 'aceites.jpg', 2, 22, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (12, 'Papas Fritas y Snacks Salados', 'papas-fritas-snacks', NULL, 'papas-fritas.jpg', 3, 31, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (13, 'Chocolates y Dulces', 'chocolates-dulces', NULL, 'chocolates.jpg', 3, 32, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (14, 'Leches', 'leches', NULL, 'leches.jpg', 4, 41, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (15, 'Yogures y Postres Lácteos', 'yogures-postres', NULL, 'yogures.jpg', 4, 42, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (16, 'Productos de Limpieza', 'productos-limpieza', 'Detergentes, suavizantes, limpiavidrios, etc.', 'detergentes.jpg', 5, 51, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (17, 'Papel y Desechables', 'papel-desechables', 'Papel higiénico, servilletas, papel toalla', 'papel-higienico.jpg', 5, 52, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (18, 'Cuidado Capilar y Corporal', 'cuidado-capilar-corporal', NULL, 'shampoo.jpg', 6, 61, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (19, 'Frutas y Verduras', 'frutas-verduras', NULL, 'frutas-verduras.jpg', 7, 71, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');
INSERT INTO `ecommerce_categorias` VALUES (20, 'Embutidos y Fiambres', 'embutidos-fiambres', NULL, 'embutidos.jpg', 7, 72, 1, '2026-01-26 02:26:10', '2026-01-26 02:26:10');

-- ----------------------------
-- Table structure for ecommerce_clientes_olddddd
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_clientes_olddddd`;
CREATE TABLE `ecommerce_clientes_olddddd`  (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NULL DEFAULT NULL COMMENT 'Relación con tabla users',
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellidos` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `documento_tipo` enum('DNI','RUC','CE','PASAPORTE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'DNI',
  `documento_numero` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fecha_nacimiento` date NULL DEFAULT NULL,
  `genero` enum('M','F','O') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email_verificado_at` timestamp NULL DEFAULT NULL,
  `remember_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_cliente`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE,
  INDEX `idx_documento`(`documento_numero` ASC) USING BTREE,
  INDEX `idx_activo`(`activo` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_clientes_olddddd
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_configuracion
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_configuracion`;
CREATE TABLE `ecommerce_configuracion`  (
  `id_config` int NOT NULL AUTO_INCREMENT,
  `clave` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `tipo` enum('string','number','boolean','json') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'string',
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_config`) USING BTREE,
  UNIQUE INDEX `clave`(`clave` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_configuracion
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_cupones
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_cupones`;
CREATE TABLE `ecommerce_cupones`  (
  `id_cupon` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tipo_descuento` enum('porcentaje','monto_fijo') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor_descuento` decimal(10, 2) NOT NULL,
  `monto_minimo` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Compra mínima requerida',
  `descuento_maximo` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Tope de descuento',
  `limite_usos` int NULL DEFAULT NULL,
  `usos_actuales` int NULL DEFAULT 0,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_cupon`) USING BTREE,
  UNIQUE INDEX `codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_fechas`(`fecha_inicio` ASC, `fecha_fin` ASC) USING BTREE,
  INDEX `idx_activo`(`activo` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_cupones
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_direcciones
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_direcciones`;
CREATE TABLE `ecommerce_direcciones`  (
  `id_direccion` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NULL DEFAULT NULL,
  `nombre_completo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ej: Casa, Oficina',
  `destinatario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `departamento` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `provincia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `distrito` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `codigo_postal` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitud` decimal(10, 8) NULL DEFAULT NULL,
  `longitud` decimal(11, 8) NULL DEFAULT NULL,
  `es_principal` tinyint(1) NULL DEFAULT 0,
  `tipo` enum('envio','facturacion') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'envio',
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_direccion`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_direcciones
-- ----------------------------
INSERT INTO `ecommerce_direcciones` VALUES (1, 1, 'Casa', 'Juan Carlos Pérez', '987654321', 'Av. Perú 245, Urb. Los Pinos', 'Frente al parque Los Pinos', 'Lima', 'Lima', 'San Martín de Porres', '02100', -12.03330000, -77.05830000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (2, 1, 'Oficina', 'Juan Pérez', '987654321', 'Av. Alfredo Mendiola 1234, Of. 305', 'Edif. Empresarial Norte, piso 3', 'Lima', 'Lima', 'Independencia', '02101', -11.99800000, -77.05500000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (3, 1, 'Casa mamá', 'Sra. Gómez', '987654322', 'Jr. Huaylas 890', 'Al lado de farmacia Inkafarma', 'Lima', 'Lima', 'Comas', '02001', -11.93330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (4, 1, 'Facturación', 'Juan Carlos Pérez Gómez', '987654321', 'Av. Perú 245, Urb. Los Pinos', NULL, 'Lima', 'Lima', 'San Martín de Porres', '02100', -12.03330000, -77.05830000, 0, 'facturacion', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (5, 2, 'Casa Principal', 'María Elena Torres', '956123789', 'Jr. Huaraz 780', '2do piso, puerta azul', 'Lima', 'Lima', 'Comas', '02001', -11.93330000, -77.06670000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (6, 2, 'Trabajo', 'María Torres', '956123789', 'Av. Universitaria 4567', 'Edif. Plaza Norte, local 12', 'Lima', 'Lima', 'Los Olivos', '02102', -11.98330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (7, 2, 'Casa familiar', 'Elena Ruiz', '956123790', 'Calle Las Gardenias 321', 'Urb. Los Jazmines', 'Lima', 'Lima', 'Carabayllo', '02103', -11.88330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (8, 2, 'Departamento', 'María E. Torres', '956123789', 'Av. Tupac Amaru 1500, Dpto 401', 'Condominio Miraflores Alto', 'Lima', 'Lima', 'San Juan de Miraflores', '15801', -12.16670000, -76.96670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (9, 2, 'Facturación', 'María Elena Torres Ruiz', '956123789', 'Jr. Huaraz 780', NULL, 'Lima', 'Lima', 'Comas', '02001', -11.93330000, -77.06670000, 0, 'facturacion', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (10, 3, 'Casa', 'José A. Ramírez', '991234567', 'Calle Los Jazmines 123', 'Urb. Ate Vitarte', 'Lima', 'Lima', 'Ate', '15034', -12.01670000, -76.91670000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (11, 3, 'Oficina', 'José Ramírez', '991234567', 'Av. Javier Prado Este 4200', 'Torre Interbank, piso 8', 'Lima', 'Lima', 'San Isidro', '15046', -12.08330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (12, 3, 'Casa hermana', 'Antonio Ramírez', '991234568', 'Av. La Molina 890', 'Urb. Los Eucaliptos', 'Lima', 'Lima', 'La Molina', '15023', -12.08330000, -76.93330000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (13, 4, 'Casa Principal', 'Rosa Vásquez', '934567890', 'Av. Alfredo Mendiola 456', 'Altura cdra. 12', 'Lima', 'Lima', 'Independencia', '02101', -11.99800000, -77.05500000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (14, 4, 'Trabajo', 'Rosa M. Vásquez', '934567890', 'Jr. Amazonas 567', 'Cercado de Lima', 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (15, 4, 'Casa de la tía', 'Mercedes Díaz', '934567891', 'Av. Perú 1234', 'Urb. El Carmen', 'Lima', 'Lima', 'Puente Piedra', '15101', -11.86670000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (16, 4, 'Departamento playa', 'Rosa Vásquez', '934567890', 'Malecón de Chorrillos s/n', 'Edif. Costa Verde', 'Lima', 'Lima', 'Chorrillos', '15064', -12.18330000, -77.01670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (17, 4, 'Facturación', 'Rosa Mercedes Vásquez Díaz', '934567890', 'Av. Alfredo Mendiola 456', NULL, 'Lima', 'Lima', 'Independencia', '02101', -11.99800000, -77.05500000, 0, 'facturacion', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (18, 5, 'Casa', 'Carlos Mendoza', '999888777', 'Pasaje Las Flores s/n', 'Villa El Salvador', 'Lima', 'Lima', 'Villa El Salvador', '15842', -12.21670000, -76.93330000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (19, 5, 'Oficina', 'Carlos E. Mendoza', '999888777', 'Av. Primavera 2500', 'Surco', 'Lima', 'Lima', 'Santiago de Surco', '15023', -12.15000000, -77.00000000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (20, 7, 'Casa', 'Luis Quispe', '966554433', 'Calle Las Dalias 89', 'Los Olivos', 'Lima', 'Lima', 'Los Olivos', '02102', -11.98330000, -77.06670000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (21, 7, 'Trabajo', 'Luis F. Quispe', '966554433', 'Av. Universitaria 3000', 'Plaza Norte', 'Lima', 'Lima', 'Independencia', '02101', -11.99800000, -77.05500000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (22, 7, 'Casa padres', 'Fernando Quispe', '966554434', 'Jr. Huaral 456', 'Comas', 'Lima', 'Lima', 'Comas', '02001', -11.93330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (23, 7, 'Facturación principal', 'Luis Fernando Quispe Huamán', '966554433', 'Calle Las Dalias 89', NULL, 'Lima', 'Lima', 'Los Olivos', '02102', -11.98330000, -77.06670000, 0, 'facturacion', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (24, 7, 'Oficina secundaria', 'Luis Quispe H.', '966554433', 'Av. La Marina 1800', 'Pueblo Libre', 'Lima', 'Lima', 'Pueblo Libre', '15084', -12.08330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (25, 8, 'Casa', 'Carmen Flores', '912345678', 'Av. Tupac Amaru 1450', 'San Juan de Miraflores', 'Lima', 'Lima', 'San Juan de Miraflores', '15801', -12.16670000, -76.96670000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (26, 8, 'Departamento', 'Rosa Flores', '912345678', 'Av. Benavides 500', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (27, 10, 'Casa Principal', 'Karla Morales', '944332211', 'Urb. Las Palmeras Mz. B Lt. 15', NULL, 'Lima', 'Lima', 'Puente Piedra', '15101', -11.86670000, -77.06670000, 1, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (28, 10, 'Trabajo', 'Karla A. Morales', '944332211', 'Av. Perú 2000', 'Callao', 'Lima', 'Lima', 'Callao', '07001', -12.06670000, -77.15000000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (29, 10, 'Casa abuela', 'Andrea Chávez', '944332212', 'Jr. Ayacucho 300', 'Breña', 'Lima', 'Lima', 'Breña', '15003', -12.06670000, -77.05000000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (30, 10, 'Oficina', 'K. Morales', '944332211', 'Av. Arequipa 2500', 'Lince', 'Lima', 'Lima', 'Lince', '15046', -12.08330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (31, 10, 'Facturación', 'Karla Andrea Morales Chávez', '944332211', 'Urb. Las Palmeras Mz. B Lt. 15', NULL, 'Lima', 'Lima', 'Puente Piedra', '15101', -11.86670000, -77.06670000, 0, 'facturacion', 1, '2026-01-26 02:43:53', '2026-01-26 02:43:53');
INSERT INTO `ecommerce_direcciones` VALUES (32, 6, 'Casa Principal', 'Sonia Rojas', '988776655', 'Calle Los Pinos 567', 'Altura cdra. 5, Surquillo', 'Lima', 'Lima', 'Surquillo', '15048', -12.11670000, -77.01670000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (33, 6, 'Trabajo', 'Sonia E. Rojas', '988776655', 'Av. Larco 890', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15046', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (34, 6, 'Casa de hermana', 'Elizabeth Guzmán', '988776656', 'Jr. Iquitos 123', 'Jesús María', 'Lima', 'Lima', 'Jesús María', '15072', -12.06670000, -77.05000000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (35, 9, 'Casa', 'Pedro Sánchez', '955667788', 'Jr. Amazonas 320', 'Cercado de Lima', 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (36, 9, 'Oficina', 'P. P. Sánchez', '955667788', 'Av. Arequipa 1800', 'Lince', 'Lima', 'Lima', 'Lince', '15046', -12.08330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (37, 9, 'Casa familiar', 'Pablo Sánchez', '955667789', 'Av. Colonial 567', 'Rímac', 'Lima', 'Lima', 'Rímac', '15331', -12.03330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (38, 9, 'Facturación', 'Pedro Pablo Sánchez Vargas', '955667788', 'Jr. Amazonas 320', NULL, 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 0, 'facturacion', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (39, 11, 'Casa', 'Miguel Cruz', '977889900', 'Av. Perú 890', 'Callao', 'Lima', 'Lima', 'Callao', '07001', -12.06670000, -77.15000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (40, 11, 'Trabajo puerto', 'Miguel A. Cruz', '977889900', 'Av. Contralmirante Mora 1200', 'Bellavista - Callao', 'Lima', 'Lima', 'Bellavista', '07006', -12.06670000, -77.13330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (41, 11, 'Departamento', 'Ángel Cruz', '977889901', 'Malecón Cisneros 300', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (42, 12, 'Casa Principal', 'Patricia Luyo', '966554422', 'Jr. Ayacucho 450', 'Breña', 'Lima', 'Lima', 'Breña', '15003', -12.06670000, -77.05000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (43, 12, 'Oficina', 'P. Luyo', '966554422', 'Av. Garcilaso de la Vega 1200', 'Cercado', 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (44, 12, 'Casa padres', 'Fernández', '966554423', 'Av. Brasil 890', 'Pueblo Libre', 'Lima', 'Lima', 'Pueblo Libre', '15084', -12.08330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (45, 13, 'Casa', 'Víctor Herrera', '955443322', 'Av. Colonial 1234', 'Rímac', 'Lima', 'Lima', 'Rímac', '15331', -12.03330000, -77.03330000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (46, 13, 'Trabajo', 'Víctor M. Herrera', '955443322', 'Jr. de la Unión 567', 'Centro Histórico', 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (47, 13, 'Casa de primo', 'Manuel Soto', '955443323', 'Av. Abancay 450', 'La Victoria', 'Lima', 'Lima', 'La Victoria', '15033', -12.06670000, -77.01670000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (48, 13, 'Facturación', 'Víctor Manuel Herrera Soto', '955443322', 'Av. Colonial 1234', NULL, 'Lima', 'Lima', 'Rímac', '15331', -12.03330000, -77.03330000, 0, 'facturacion', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (49, 14, 'Casa', 'Liliana Campos', '944556677', 'Urb. Santa Rosa Mz. K Lt. 8', 'Villa María del Triunfo', 'Lima', 'Lima', 'Villa María del Triunfo', '15836', -12.16670000, -76.95000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (50, 14, 'Trabajo', 'L. Campos', '944556677', 'Av. México 1500', 'Cercado', 'Lima', 'Lima', 'Cercado de Lima', '15001', -12.04640000, -77.04280000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (51, 14, 'Casa familiar', 'Campos Vargas', '944556678', 'Av. Lima 3200', 'Villa El Salvador', 'Lima', 'Lima', 'Villa El Salvador', '15842', -12.21670000, -76.93330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (52, 15, 'Casa Principal', 'Edwin Palomino', '977112233', 'Calle Los Libertadores 890', 'San Borja', 'Lima', 'Lima', 'San Borja', '15037', -12.10000000, -77.00000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (53, 15, 'Oficina', 'Edwin P. Ruiz', '977112233', 'Av. Javier Prado Este 5000', 'Surco', 'Lima', 'Lima', 'Santiago de Surco', '15023', -12.15000000, -77.00000000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (54, 15, 'Departamento', 'Palomino', '977112234', 'Av. Primavera 1800', 'Surco', 'Lima', 'Lima', 'Santiago de Surco', '15023', -12.15000000, -77.00000000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (55, 15, 'Facturación LP', 'Edwin Palomino Ruiz', '977112233', 'Calle Los Libertadores 890', NULL, 'Lima', 'Lima', 'San Borja', '15037', -12.10000000, -77.00000000, 0, 'facturacion', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (56, 15, 'Casa secundaria', 'E. Palomino', '977112233', 'Av. Benavides 1200', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (57, 16, 'Casa', 'Gloria Montes', '988990011', 'Av. Angamos Este 1800', 'Surco', 'Lima', 'Lima', 'Santiago de Surco', '15023', -12.15000000, -77.00000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (58, 16, 'Trabajo', 'Gloria M. Oca', '988990011', 'Av. Larco 1500', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15046', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (59, 17, 'Casa Principal', 'Fernando Valdivia', '933445566', 'Jr. Iquitos 567', 'Jesús María', 'Lima', 'Lima', 'Jesús María', '15072', -12.06670000, -77.05000000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (60, 17, 'Oficina', 'F. Valdivia', '933445566', 'Av. Brasil 2000', 'Pueblo Libre', 'Lima', 'Lima', 'Pueblo Libre', '15084', -12.08330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (61, 17, 'Casa de hermano', 'Valdivia Ponce', '933445567', 'Av. Sucre 890', 'Pueblo Libre', 'Lima', 'Lima', 'Pueblo Libre', '15084', -12.08330000, -77.06670000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (62, 18, 'Casa', 'Verónica Espinoza', '966778899', 'Pasaje Los Rosales 45', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (63, 18, 'Trabajo', 'V. Espinoza', '966778899', 'Av. José Larco 800', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15046', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (64, 18, 'Departamento playa', 'Espinoza Díaz', '966778900', 'Malecón Balta 200', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (65, 19, 'Casa', 'Cliente 19 Ejemplo', '999000111', 'Av. La Marina 3000', 'Pueblo Libre', 'Lima', 'Lima', 'Pueblo Libre', '15084', -12.08330000, -77.06670000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (66, 19, 'Oficina', 'Ejemplo 19', '999000111', 'Av. Javier Prado 3500', 'San Isidro', 'Lima', 'Lima', 'San Isidro', '15046', -12.08330000, -77.03330000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (67, 20, 'Casa Principal', 'Cliente 20 Ejemplo', '988112233', 'Av. Benavides 2500', 'Miraflores', 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 1, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (68, 20, 'Trabajo', 'Ejemplo 20', '988112233', 'Av. Angamos Oeste 1200', 'Surquillo', 'Lima', 'Lima', 'Surquillo', '15048', -12.11670000, -77.01670000, 0, 'envio', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');
INSERT INTO `ecommerce_direcciones` VALUES (69, 20, 'Facturación', 'Cliente Ejemplo 20', '988112233', 'Av. Benavides 2500', NULL, 'Lima', 'Lima', 'Miraflores', '15048', -12.11670000, -77.03330000, 0, 'facturacion', 1, '2026-01-26 02:44:14', '2026-01-26 02:44:14');

-- ----------------------------
-- Table structure for ecommerce_marcas
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_marcas`;
CREATE TABLE `ecommerce_marcas`  (
  `id_marca` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_marca`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_marcas
-- ----------------------------
INSERT INTO `ecommerce_marcas` VALUES (1, 'Coca-Cola', 'coca-cola', 'Bebidas gaseosas líder mundial', 'coca-cola-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (2, 'Inca Kola', 'inca-kola', 'La bebida peruana por excelencia', 'inka-kola-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (3, 'San Luis', 'san-luis', 'Agua mineral peruana de Backus', 'san-luis-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (4, 'Cielo', 'cielo', 'Agua purificada de bajo costo', 'cielo-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (5, 'Lay\'s', 'lays', 'Papas fritas y snacks salados', 'lays-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (6, 'Cheetos', 'cheetos', 'Snacks de maíz con queso', 'cheetos-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (7, 'Doritos', 'doritos', 'Totopos y snacks picantes', 'doritos-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (8, 'Sublime', 'sublime', 'Chocolate peruano clásico de Nestlé', 'sublime-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (9, 'Oreo', 'oreo', 'Galletas con crema', 'oreo-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (10, 'Casino', 'casino', 'Galletas dulces y saladas peruanas', 'casino-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (11, 'Gloria', 'gloria', 'Leches, yogures y productos lácteos líder en Perú', 'gloria-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (12, 'Costeño', 'costeno', 'Arroz peruano de alta calidad', 'costeno-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (13, 'Nicolini', 'nicolini', 'Aceites y productos de cocina peruanos', 'nicolini-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (14, 'Rubí', 'rubi', 'Azúcar blanca tradicional', 'rubi-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (15, 'Ajinomoto', 'ajinomoto', 'Sazonadores y condimentos', 'ajinomoto-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (16, 'Bolívar', 'bolivar', 'Jabones de tocador económicos', 'bolivar-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (17, 'Head & Shoulders', 'head-shoulders', 'Shampoo anticaspa', 'head-shoulders-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (18, 'Colgate', 'colgate', 'Pastas dentales y cuidado bucal', 'colgate-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (19, 'Sapolio', 'sapolio', 'Limpiadores multiuso y jabones en barra', 'sapolio-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (20, 'Nova', 'nova', 'Papel toalla y servilletas', 'nova-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (21, 'Fosforos El Sol', 'fosforos-el-sol', 'Fósforos clásicos peruanos', 'el-sol-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (22, 'Donofrio', 'donofrio', 'Helados y postres congelados (aunque sea minimarket pequeño)', 'donofrio-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (23, 'Campo Grande', 'campo-grande', 'Embutidos y fiambres peruanos', 'campo-grande-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (24, 'Laive', 'laive', 'Leches y lácteos alternativos', 'laive-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');
INSERT INTO `ecommerce_marcas` VALUES (25, 'Faraon', 'faraon', 'Fideos y pastas económicas', 'faraon-logo.png', 1, '2026-01-26 02:27:01', '2026-01-26 02:27:01');

-- ----------------------------
-- Table structure for ecommerce_metodos_envio
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_metodos_envio`;
CREATE TABLE `ecommerce_metodos_envio`  (
  `id_metodo_envio` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `precio` decimal(10, 2) NULL DEFAULT NULL,
  `tiempo_entrega` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ej: 2-3 días hábiles',
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tiempo_entrega_min` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tiempo_entrega_max` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_metodo_envio`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_metodos_envio
-- ----------------------------
INSERT INTO `ecommerce_metodos_envio` VALUES (1, 'Delivery', 'Por motorizado', 10.00, '5', 1, '2026-01-25 03:18:07', '2026-01-25 07:26:33', '2', '10');
INSERT INTO `ecommerce_metodos_envio` VALUES (2, 'Presencial', 'Se acerca', 0.00, '12', 1, '2026-01-25 03:18:26', '2026-01-25 07:26:36', '4', '44');
INSERT INTO `ecommerce_metodos_envio` VALUES (3, 'Contra entrega', 'Paga y lo recibe', 20.00, '10', 1, '2026-01-25 03:18:45', '2026-01-25 07:26:38', '33', '45');

-- ----------------------------
-- Table structure for ecommerce_metodos_pago
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_metodos_pago`;
CREATE TABLE `ecommerce_metodos_pago`  (
  `id_metodo_pago` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `icono` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `comision` decimal(5, 2) NULL DEFAULT 0.00 COMMENT 'Porcentaje de comisión',
  `configuracion` json NULL COMMENT 'Credenciales API, etc',
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_metodo_pago`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_metodos_pago
-- ----------------------------
INSERT INTO `ecommerce_metodos_pago` VALUES (1, 'Yape', NULL, NULL, 5.00, NULL, 1, '2026-01-25 03:19:18', '2026-01-25 03:19:18');
INSERT INTO `ecommerce_metodos_pago` VALUES (2, 'Efectivo', NULL, NULL, 0.00, NULL, 1, '2026-01-25 03:19:37', '2026-01-25 03:19:37');

-- ----------------------------
-- Table structure for ecommerce_notificaciones
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_notificaciones`;
CREATE TABLE `ecommerce_notificaciones`  (
  `id_notificacion` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `tipo` enum('pedido','promocion','sistema','precio') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `titulo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `mensaje` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `datos` json NULL,
  `leida` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_notificacion`) USING BTREE,
  INDEX `idx_cliente`(`id_cliente` ASC) USING BTREE,
  INDEX `idx_leida`(`leida` ASC) USING BTREE,
  INDEX `idx_tipo`(`tipo` ASC) USING BTREE,
  CONSTRAINT `ecommerce_notificaciones_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `ecommerce_clientes_olddddd` (`id_cliente`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_notificaciones
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_pedido_historial
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_pedido_historial`;
CREATE TABLE `ecommerce_pedido_historial`  (
  `id_historial` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `estado_anterior` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `estado_nuevo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `id_usuario` int NULL DEFAULT NULL COMMENT 'Usuario admin que hizo el cambio',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creado_por` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_historial`) USING BTREE,
  INDEX `idx_pedido`(`id_pedido` ASC) USING BTREE,
  CONSTRAINT `ecommerce_pedido_historial_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_pedido_historial
-- ----------------------------
INSERT INTO `ecommerce_pedido_historial` VALUES (1, 5, NULL, 'pendiente', 'Pedido creado', NULL, '2026-01-25 15:33:07', 'sistema', '2026-01-25 15:33:07');
INSERT INTO `ecommerce_pedido_historial` VALUES (2, 11, NULL, 'pendiente', 'Pedido creado', NULL, '2026-01-29 07:54:21', 'sistema', '2026-01-29 07:54:21');
INSERT INTO `ecommerce_pedido_historial` VALUES (3, 12, NULL, 'pendiente', 'Pedido creado', NULL, '2026-01-29 08:03:22', 'sistema', '2026-01-29 08:03:22');
INSERT INTO `ecommerce_pedido_historial` VALUES (4, 13, NULL, 'pendiente', 'Pedido creado', NULL, '2026-01-29 08:06:24', 'sistema', '2026-01-29 08:06:24');
INSERT INTO `ecommerce_pedido_historial` VALUES (5, 13, 'pendiente', 'procesando', 'esta en almacen', NULL, '2026-01-29 08:38:02', 'ADMIN', '2026-01-29 08:38:02');
INSERT INTO `ecommerce_pedido_historial` VALUES (6, 5, 'entregado', 'entregado', 'Pago revertido por administrador', NULL, '2026-01-29 10:43:22', 'ADMIN', '2026-01-29 10:43:22');
INSERT INTO `ecommerce_pedido_historial` VALUES (7, 5, 'entregado', 'entregado', 'Pago revertido por administrador', NULL, '2026-01-29 10:43:34', 'ADMIN', '2026-01-29 10:43:34');
INSERT INTO `ecommerce_pedido_historial` VALUES (8, 13, 'procesando', 'procesando', 'Pago revertido por administrador', NULL, '2026-01-29 10:55:41', 'ADMIN', '2026-01-29 10:55:41');
INSERT INTO `ecommerce_pedido_historial` VALUES (9, 13, 'procesando', 'procesando', 'Pago revertido por administrador', NULL, '2026-01-29 11:00:27', 'ADMIN', '2026-01-29 11:00:27');
INSERT INTO `ecommerce_pedido_historial` VALUES (10, 13, 'procesando', 'procesando', 'Pago revertido por administrador', NULL, '2026-01-29 11:05:47', 'ADMIN', '2026-01-29 11:05:47');

-- ----------------------------
-- Table structure for ecommerce_pedido_items
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_pedido_items`;
CREATE TABLE `ecommerce_pedido_items`  (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `id_producto` int NOT NULL,
  `id_variante` int NULL DEFAULT NULL,
  `nombre_producto` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `variante_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ej: Talla: XL, Color: Rojo',
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10, 2) NOT NULL,
  `subtotal` decimal(10, 2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_item`) USING BTREE,
  INDEX `id_producto`(`id_producto` ASC) USING BTREE,
  INDEX `idx_pedido`(`id_pedido` ASC) USING BTREE,
  CONSTRAINT `ecommerce_pedido_items_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_pedido_items_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_pedido_items
-- ----------------------------
INSERT INTO `ecommerce_pedido_items` VALUES (3, 5, 12, NULL, 'asdasdas', '3', NULL, 1, 1.00, 1.00, '2026-01-25 15:33:07', '2026-01-25 15:33:07');
INSERT INTO `ecommerce_pedido_items` VALUES (4, 11, 13, NULL, 'Coca Cola 1.5 L', 'CC15LR', NULL, 1, 8.50, 8.50, '2026-01-29 07:54:21', '2026-01-29 07:54:21');
INSERT INTO `ecommerce_pedido_items` VALUES (5, 12, 14, NULL, 'Inka Cola 1 L', 'IK1L', NULL, 1, 6.20, 6.20, '2026-01-29 08:03:22', '2026-01-29 08:03:22');
INSERT INTO `ecommerce_pedido_items` VALUES (6, 13, 14, NULL, 'Inka Cola 1 L', 'IK1L', NULL, 2, 6.20, 12.40, '2026-01-29 08:06:24', '2026-01-29 08:06:24');
INSERT INTO `ecommerce_pedido_items` VALUES (7, 13, 13, NULL, 'Coca Cola 1.5 L', 'CC15LR', NULL, 1, 8.50, 8.50, '2026-01-29 08:06:24', '2026-01-29 08:06:24');

-- ----------------------------
-- Table structure for ecommerce_pedido_tracking
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_pedido_tracking`;
CREATE TABLE `ecommerce_pedido_tracking`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NULL DEFAULT NULL,
  `id_estado` int NULL DEFAULT NULL,
  `id_etapa` int NULL DEFAULT NULL COMMENT 'Etapa actual del recorrido',
  `codigo_rastreo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Código único de rastreo público',
  `transportista` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `numero_guia` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url_transportista` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'URL de tracking del transportista',
  `fecha_estimada_entrega` date NULL DEFAULT NULL,
  `fecha_entrega_real` datetime NULL DEFAULT NULL,
  `direccion_actual` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitud` decimal(10, 8) NULL DEFAULT NULL,
  `longitud` decimal(11, 8) NULL DEFAULT NULL,
  `notas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `codigo_rastreo`(`codigo_rastreo` ASC) USING BTREE,
  INDEX `etapa_id`(`id_etapa` ASC) USING BTREE,
  INDEX `idx_pedido`(`id_pedido` ASC) USING BTREE,
  INDEX `idx_codigo_rastreo`(`codigo_rastreo` ASC) USING BTREE,
  INDEX `idx_estado`(`id_estado` ASC) USING BTREE,
  CONSTRAINT `ecommerce_pedido_tracking_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_pedido_tracking_ibfk_2` FOREIGN KEY (`id_estado`) REFERENCES `ecommerce_tracking_estados` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_pedido_tracking_ibfk_3` FOREIGN KEY (`id_etapa`) REFERENCES `ecommerce_tracking_etapas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_pedido_tracking
-- ----------------------------
INSERT INTO `ecommerce_pedido_tracking` VALUES (1, 5, 9, 7, '234234', 'CARLOS GUTIERRES', '00125', NULL, '2026-01-28', '2026-01-26 00:40:20', '324', NULL, NULL, 'zsdczcxz', '2026-01-25 10:47:59', '2026-01-26 00:40:20');
INSERT INTO `ecommerce_pedido_tracking` VALUES (7, 13, 1, 1, 'TRK202600000013', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');

-- ----------------------------
-- Table structure for ecommerce_pedidos
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_pedidos`;
CREATE TABLE `ecommerce_pedidos`  (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `numero_pedido` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `id_cliente` int NULL DEFAULT NULL,
  `envio_destinatario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_referencia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_departamento` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_provincia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_distrito` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `envio_codigo_postal` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `facturacion_tipo_documento` enum('BOLETA','FACTURA') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'BOLETA',
  `facturacion_ruc` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `facturacion_razon_social` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `facturacion_direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `subtotal` decimal(10, 2) NULL DEFAULT NULL,
  `descuento` decimal(10, 2) NULL DEFAULT 0.00,
  `codigo_cupon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `costo_envio` decimal(10, 2) NULL DEFAULT 0.00,
  `igv` decimal(10, 2) NULL DEFAULT NULL,
  `total` decimal(10, 2) NULL DEFAULT NULL,
  `estado` enum('pendiente','confirmado','procesando','enviado','entregado','cancelado','reembolsado') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'pendiente',
  `estado_pago` enum('pendiente','pagado','fallido','reembolsado') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'pendiente',
  `id_metodo_envio` int NULL DEFAULT NULL,
  `numero_seguimiento` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `transportista` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fecha_envio` timestamp NULL DEFAULT NULL,
  `fecha_entrega_estimada` date NULL DEFAULT NULL,
  `fecha_entrega` timestamp NULL DEFAULT NULL,
  `id_metodo_pago` int NULL DEFAULT NULL,
  `transaccion_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `fecha_pago` timestamp NULL DEFAULT NULL,
  `notas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `notas_internas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `pagado` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_pedido`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_pedidos
-- ----------------------------
INSERT INTO `ecommerce_pedidos` VALUES (5, 'ORD-2026-00001', 8, NULL, NULL, '{\"nombre\":\"casa\",\"telefono\":\"970048451\",\"direccion\":\"MZ I 12 LT 8 JOSE CARLOS MARIATEGUI\",\"direccion_adicional\":null,\"ciudad\":null,\"estado\":null,\"codigo_postal\":\"01\",\"pais\":\"Per\\u00fa\"}', NULL, NULL, NULL, NULL, NULL, 'BOLETA', NULL, NULL, NULL, 1.00, 0.00, NULL, 0.00, NULL, 21.18, 'entregado', 'pendiente', 3, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '234234234', NULL, '2026-01-25 15:33:07', '2026-01-29 10:43:34', NULL, '0');
INSERT INTO `ecommerce_pedidos` VALUES (11, 'ORD-2026-00002', 20, NULL, NULL, '{\"nombre\":\"Trabajo\",\"telefono\":\"988112233\",\"direccion\":\"Av. Angamos Oeste 1200\",\"direccion_adicional\":\"Surquillo\",\"ciudad\":\"Surquillo\",\"estado\":\"Lima\",\"codigo_postal\":\"15048\",\"pais\":\"Per\\u00fa\"}', NULL, NULL, NULL, NULL, NULL, 'BOLETA', NULL, NULL, NULL, 8.50, 0.00, NULL, 0.00, NULL, 20.03, 'pendiente', 'pendiente', 1, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, 'dasdasd', NULL, '2026-01-29 07:54:21', '2026-01-29 07:54:21', NULL, NULL);
INSERT INTO `ecommerce_pedidos` VALUES (12, 'ORD-2026-00003', 18, NULL, NULL, '{\"nombre\":\"Trabajo\",\"telefono\":\"966778899\",\"direccion\":\"Av. Jos\\u00e9 Larco 800\",\"direccion_adicional\":\"Miraflores\",\"ciudad\":\"Miraflores\",\"estado\":\"Lima\",\"codigo_postal\":\"15046\",\"pais\":\"Per\\u00fa\"}', NULL, NULL, NULL, NULL, NULL, 'BOLETA', NULL, NULL, NULL, 6.20, 0.00, NULL, 0.00, NULL, 7.32, 'pendiente', 'pendiente', 2, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 'sasdad', NULL, '2026-01-29 08:03:22', '2026-01-29 08:03:22', NULL, NULL);
INSERT INTO `ecommerce_pedidos` VALUES (13, 'ORD-2026-00004', 18, NULL, NULL, '{\"nombre\":\"Departamento playa\",\"telefono\":\"966778900\",\"direccion\":\"Malec\\u00f3n Balta 200\",\"direccion_adicional\":\"Miraflores\",\"ciudad\":\"Miraflores\",\"estado\":\"Lima\",\"codigo_postal\":\"15048\",\"pais\":\"Per\\u00fa\"}', NULL, NULL, NULL, NULL, NULL, 'BOLETA', NULL, NULL, NULL, 20.90, 0.00, NULL, 0.00, NULL, 24.66, 'procesando', 'pendiente', 2, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, 'asdaDCA', NULL, '2026-01-29 08:06:24', '2026-01-29 11:05:47', NULL, '0');

-- ----------------------------
-- Table structure for ecommerce_producto_imagenes
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_producto_imagenes`;
CREATE TABLE `ecommerce_producto_imagenes`  (
  `id_imagen` int NOT NULL AUTO_INCREMENT,
  `id_producto` int NOT NULL,
  `imagen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `orden` int NULL DEFAULT 0,
  `es_principal` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_imagen`) USING BTREE,
  INDEX `idx_producto`(`id_producto` ASC) USING BTREE,
  CONSTRAINT `ecommerce_producto_imagenes_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_producto_imagenes
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_producto_variantes
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_producto_variantes`;
CREATE TABLE `ecommerce_producto_variantes`  (
  `id_variante` int NOT NULL AUTO_INCREMENT,
  `id_producto` int NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ej: Talla, Color',
  `valor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ej: XL, Rojo',
  `precio_adicional` decimal(10, 2) NULL DEFAULT 0.00,
  `stock` int NULL DEFAULT 0,
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_variante`) USING BTREE,
  INDEX `idx_producto`(`id_producto` ASC) USING BTREE,
  CONSTRAINT `ecommerce_producto_variantes_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_producto_variantes
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_productos
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_productos`;
CREATE TABLE `ecommerce_productos`  (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `slug` varchar(220) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `descripcion_corta` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `precio` decimal(10, 2) NOT NULL,
  `precio_oferta` decimal(10, 2) NULL DEFAULT NULL,
  `costo` decimal(10, 2) NULL DEFAULT NULL,
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `codigo_barras` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `stock` int NULL DEFAULT 0,
  `stock_minimo` int NULL DEFAULT 5,
  `id_categoria` int NOT NULL,
  `id_marca` int NULL DEFAULT NULL,
  `peso` decimal(8, 2) NULL DEFAULT NULL COMMENT 'Peso en kg',
  `dimensiones` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'LxAxH en cm',
  `destacado` tinyint(1) NULL DEFAULT 0,
  `nuevo` tinyint(1) NULL DEFAULT 0,
  `en_oferta` tinyint(1) NULL DEFAULT 0,
  `rating` decimal(2, 1) NULL DEFAULT 0.0,
  `total_reviews` int NULL DEFAULT 0,
  `total_ventas` int NULL DEFAULT 0,
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `imagen_principal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `imagenes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `especificaciones` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_producto`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_productos
-- ----------------------------
INSERT INTO `ecommerce_productos` VALUES (12, 'asdasdas', 'w3', '3333', '333', 1.00, 1.00, 1.00, '3', NULL, 0, 5, 3, NULL, 3.00, '3', 0, 1, 0, 0.0, 0, 0, 1, '2026-01-24 21:03:24', '2026-01-25 15:33:07', NULL, '33', 'https://upload.wikimedia.org/wikipedia/commons/7/7a/Yasuda_yogurt_150ml.jpg', NULL);
INSERT INTO `ecommerce_productos` VALUES (13, 'Coca Cola 1.5 L', 'coca-cola-15l', NULL, 'Gaseosa Coca Cola retornable 1.5 litros', 8.50, 7.80, 5.80, 'CC15LR', '7750749000108', 46, 12, 1, 5, 1.52, '15x15x28', 1, 0, 1, 4.6, 128, 645, 1, '2026-01-26 02:23:28', '2026-01-29 08:06:24', NULL, 'coca-cola-15l.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (14, 'Inka Cola 1 L', 'inka-cola-1l', NULL, 'Bebida gaseosa peruana Inka Kola 1 litro', 6.20, NULL, 4.10, 'IK1L', '7750749000306', 69, 15, 1, 5, 1.05, '10x10x25', 1, 0, 0, 4.7, 89, 412, 1, '2026-01-26 02:23:28', '2026-01-29 08:06:24', NULL, 'inka-cola-1l.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (15, 'Agua San Luis sin gas 625 ml', 'san-luis-625ml', NULL, 'Agua mineral sin gas San Luis 625 ml', 2.80, 2.50, 1.70, 'SL625', '7754349000018', 120, 24, 1, 12, 0.63, '7x7x22', 0, 0, 1, 4.4, 56, 289, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'san-luis-625.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (16, 'Lay\'s Clásica 145g', 'lays-clasica-145g', NULL, 'Papas fritas sabor original Lay\'s 145 g', 5.90, 4.90, 3.40, 'LAY145', '7750243061024', 65, 10, 2, 8, 0.15, '18x28x6', 1, 0, 1, 4.5, 94, 378, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'lays-clasica.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (17, 'Cheetos Queso 95g', 'cheetos-queso-95g', NULL, 'Snack de maíz con queso Cheetos 95 g', 4.50, NULL, 2.90, 'CHT95', '7750243065015', 88, 15, 2, 8, 0.10, '15x22x5', 0, 1, 0, 4.3, 67, 245, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'cheetos-queso.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (18, 'Doritos Nacho Queso 140g', 'doritos-nacho-140g', NULL, 'Totopos con sabor queso Doritos 140 g', 6.30, 5.50, 3.80, 'DOR140', '7750243062014', 52, 10, 2, 8, 0.14, '20x30x6', 1, 0, 1, 4.6, 112, 521, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'doritos-nacho.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (19, 'Leche Gloria entera bolsa 900 ml', 'leche-gloria-900ml', NULL, 'Leche entera bolsa Gloria 900 ml', 5.40, 4.90, 3.60, 'GLR900E', '7751271012345', 180, 30, 3, 3, 0.92, '10x5x20', 1, 0, 1, 4.7, 145, 890, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'leche-gloria-900.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (20, 'Yogurt Gloria natural 1 kg', 'yogurt-gloria-natural-1kg', NULL, 'Yogurt natural sin azúcar 1 kg', 9.80, NULL, 6.50, 'YGN1K', '7751271015678', 42, 8, 3, 3, 1.05, '12x12x18', 0, 0, 0, 4.4, 78, 312, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'yogurt-gloria-1kg.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (21, 'Arroz Costeño superior 5 kg', 'arroz-costeno-5kg', NULL, 'Arroz blanco superior Costeño 5 kg', 24.90, 22.50, 18.20, 'ARC5K', '7751232003456', 38, 10, 4, 7, 5.00, '30x45x12', 1, 0, 1, 4.8, 210, 1345, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'arroz-costeno-5kg.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (22, 'Aceite Nicolini 900 ml', 'aceite-nicolini-900ml', NULL, 'Aceite vegetal Nicolini 900 ml', 8.90, 7.90, 5.90, 'AN900', '7751234007890', 95, 15, 4, 4, 0.90, '10x10x22', 0, 0, 1, 4.5, 98, 456, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'aceite-nicolini.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (23, 'Azúcar Rubí 1 kg', 'azucar-rubi-1kg', NULL, 'Azúcar blanca Rubí 1 kg', 4.80, NULL, 3.20, 'AZR1K', '7752345001234', 140, 20, 4, 6, 1.00, '12x8x18', 0, 0, 0, 4.6, 134, 678, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'azucar-rubi.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (24, 'Jabón Bolívar coco 125g', 'jabon-bolivar-coco-125g', NULL, 'Jabón de tocador Bolívar coco 125 g', 2.30, 1.90, 1.30, 'JBVC125', '7753456002345', 210, 30, 5, 9, 0.13, '8x4x10', 1, 1, 1, 4.7, 167, 890, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'jabon-bolivar-coco.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (25, 'Shampoo Head & Shoulders 400 ml', 'head-shoulders-400ml', NULL, 'Shampoo anticaspa Head & Shoulders 400 ml', 18.90, 16.50, 11.80, 'HS400', '4902430641289', 28, 6, 5, 10, 0.42, '8x5x22', 1, 0, 1, 4.5, 89, 345, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'head-shoulders-400.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (26, 'Pasta Dental Colgate Triple Acción 150g', 'colgate-triple-150g', NULL, 'Pasta dental Colgate Triple Acción 150 g', 7.20, 6.30, 4.50, 'COLT150', '7509546067890', 75, 12, 5, 11, 0.15, '18x5x4', 0, 0, 0, 4.6, 102, 567, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'colgate-triple.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (27, 'Sublime chocolate 95g', 'sublime-95g', NULL, 'Chocolate Sublime clásico 95 g', 6.50, 5.80, 3.90, 'SUB95', '7750720001234', 85, 15, 6, 2, 0.10, '15x8x2', 1, 1, 1, 4.8, 156, 912, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'sublime-95g.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (28, 'Oreo paquete 12 und', 'oreo-12und', NULL, 'Galletas Oreo paquete x12 unidades', 8.90, 7.90, 5.60, 'ORE12', '7622300834562', 55, 10, 6, 13, 0.20, '22x15x5', 1, 0, 0, 4.7, 134, 678, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'oreo-12.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (29, 'Galletas Casino vainilla 180g', 'casino-vainilla-180g', NULL, 'Galletas Casino sabor vainilla 180 g', 5.40, NULL, 3.40, 'CAS180V', '7751235006789', 68, 12, 6, 14, 0.18, '20x12x5', 0, 0, 0, 4.4, 76, 289, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'casino-vainilla.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (30, 'Fósforos El Sol caja grande', 'fosforos-el-sol', NULL, 'Fósforos El Sol caja grande 200 unidades', 3.20, 2.80, 1.90, 'FES200', '7754567001234', 150, 25, 7, 15, 0.08, '12x5x3', 0, 0, 0, 4.5, 45, 156, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'fosforos-el-sol.jpg', NULL, NULL);
INSERT INTO `ecommerce_productos` VALUES (31, 'Papel toalla Nova 2 rollos', 'papel-toalla-nova-2rollos', NULL, 'Papel toalla Nova 2 rollos', 9.50, 8.20, 6.10, 'PTN2', '7752345012345', 45, 8, 7, 16, 0.40, '22x22x25', 1, 0, 1, 4.6, 98, 423, 1, '2026-01-26 02:23:28', '2026-01-26 02:23:28', NULL, 'papel-toalla-nova.jpg', NULL, NULL);

-- ----------------------------
-- Table structure for ecommerce_refresh_tokens
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_refresh_tokens`;
CREATE TABLE `ecommerce_refresh_tokens`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expires_at` timestamp NOT NULL,
  `revoked` tinyint(1) NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_token`(`token`(255) ASC) USING BTREE,
  INDEX `idx_cliente`(`id_cliente` ASC) USING BTREE,
  INDEX `idx_expires`(`expires_at` ASC) USING BTREE,
  CONSTRAINT `ecommerce_refresh_tokens_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `ecommerce_clientes_olddddd` (`id_cliente`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_refresh_tokens
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_reviews
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_reviews`;
CREATE TABLE `ecommerce_reviews`  (
  `id_review` int NOT NULL AUTO_INCREMENT,
  `id_producto` int NOT NULL,
  `id_cliente` int NOT NULL,
  `id_pedido` int NULL DEFAULT NULL COMMENT 'Pedido donde compró el producto',
  `rating` tinyint NOT NULL,
  `titulo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `es_compra_verificada` tinyint(1) NULL DEFAULT 0,
  `votos_utiles` int NULL DEFAULT 0,
  `aprobado` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_review`) USING BTREE,
  UNIQUE INDEX `uk_cliente_producto`(`id_cliente` ASC, `id_producto` ASC) USING BTREE,
  INDEX `idx_producto`(`id_producto` ASC) USING BTREE,
  INDEX `idx_rating`(`rating` ASC) USING BTREE,
  INDEX `idx_aprobado`(`aprobado` ASC) USING BTREE,
  CONSTRAINT `ecommerce_reviews_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_reviews_ibfk_2` FOREIGN KEY (`id_cliente`) REFERENCES `ecommerce_clientes_olddddd` (`id_cliente`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_reviews_chk_1` CHECK (`rating` between 1 and 5)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_reviews
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_tracking_estados
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_tracking_estados`;
CREATE TABLE `ecommerce_tracking_estados`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `icono` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'package',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#3f51b5',
  `orden` int NOT NULL DEFAULT 0,
  `es_final` tinyint(1) NULL DEFAULT 0 COMMENT 'Indica si es un estado final (entregado, cancelado)',
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_orden`(`orden` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_tracking_estados
-- ----------------------------
INSERT INTO `ecommerce_tracking_estados` VALUES (1, 'creado', 'Pedido Creado', 'Tu pedido ha sido registrado exitosamente', 'shopping_cart', '#9e9e9e', 1, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (2, 'confirmado', 'Pedido Confirmado', 'Tu pedido ha sido confirmado y el pago verificado', 'check_circle', '#2196f3', 2, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (3, 'preparando', 'En Preparación', 'Estamos preparando tu pedido', 'inventory_2', '#ff9800', 3, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (4, 'empacado', 'Empacado', 'Tu pedido ha sido empacado y está listo para envío', 'inventory', '#ff9800', 4, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (5, 'despachado', 'Despachado', 'Tu pedido ha sido entregado al transportista', 'local_shipping', '#673ab7', 5, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (6, 'en_transito', 'En Tránsito', 'Tu pedido está en camino', 'departure_board', '#3f51b5', 6, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (7, 'centro_distribucion', 'En Centro de Distribución', 'Tu pedido llegó al centro de distribución de tu ciudad', 'warehouse', '#00bcd4', 7, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (8, 'en_ruta', 'En Ruta de Entrega', 'Tu pedido está en ruta hacia tu dirección', 'two_wheeler', '#4caf50', 8, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (9, 'entregado', 'Entregado', 'Tu pedido ha sido entregado exitosamente', 'done_all', '#4caf50', 9, 1, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (10, 'cancelado', 'Cancelado', 'El pedido ha sido cancelado', 'cancel', '#f44336', 10, 1, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (11, 'devuelto', 'Devuelto', 'El pedido ha sido devuelto', 'undo', '#f44336', 11, 1, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_estados` VALUES (12, 'retenido', 'Retenido', 'El pedido está retenido por verificación', 'pause_circle', '#ff5722', 12, 0, 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');

-- ----------------------------
-- Table structure for ecommerce_tracking_etapas
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_tracking_etapas`;
CREATE TABLE `ecommerce_tracking_etapas`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `tipo` enum('origen','centro_distribucion','transito','destino') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `orden` int NOT NULL DEFAULT 0,
  `icono` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'location_on',
  `activo` tinyint(1) NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_tipo`(`tipo` ASC) USING BTREE,
  INDEX `idx_orden`(`orden` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_tracking_etapas
-- ----------------------------
INSERT INTO `ecommerce_tracking_etapas` VALUES (1, 'almacen_origen', 'Almacén de Origen', 'Centro de almacenamiento donde se prepara el pedido', 'origen', 1, 'store', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (2, 'centro_acopio', 'Centro de Acopio', 'Centro de consolidación de paquetes', 'centro_distribucion', 2, 'business', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (3, 'hub_nacional', 'Hub Nacional', 'Centro de distribución nacional', 'centro_distribucion', 3, 'account_balance', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (4, 'hub_regional', 'Hub Regional', 'Centro de distribución regional', 'centro_distribucion', 4, 'domain', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (5, 'centro_ciudad', 'Centro de Ciudad', 'Centro de distribución de la ciudad destino', 'centro_distribucion', 5, 'location_city', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (6, 'vehiculo_reparto', 'Vehículo de Reparto', 'En vehículo para entrega final', 'transito', 6, 'local_shipping', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');
INSERT INTO `ecommerce_tracking_etapas` VALUES (7, 'direccion_destino', 'Dirección de Entrega', 'Dirección final del cliente', 'destino', 7, 'home', 1, '2026-01-23 03:13:43', '2026-01-23 03:13:43');

-- ----------------------------
-- Table structure for ecommerce_tracking_historial
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_tracking_historial`;
CREATE TABLE `ecommerce_tracking_historial`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `id_estado` int NOT NULL,
  `id_etapa` int NULL DEFAULT NULL,
  `titulo` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `ubicacion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitud` decimal(10, 8) NULL DEFAULT NULL,
  `longitud` decimal(11, 8) NULL DEFAULT NULL,
  `operador` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Quien registró el cambio',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `metadata` json NULL COMMENT 'Datos adicionales del evento',
  `fecha_evento` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `etapa_id`(`id_etapa` ASC) USING BTREE,
  INDEX `idx_pedido`(`id_pedido` ASC) USING BTREE,
  INDEX `idx_fecha`(`fecha_evento` ASC) USING BTREE,
  INDEX `idx_estado`(`id_estado` ASC) USING BTREE,
  CONSTRAINT `ecommerce_tracking_historial_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_tracking_historial_ibfk_2` FOREIGN KEY (`id_estado`) REFERENCES `ecommerce_tracking_estados` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_tracking_historial_ibfk_3` FOREIGN KEY (`id_etapa`) REFERENCES `ecommerce_tracking_etapas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_tracking_historial
-- ----------------------------
INSERT INTO `ecommerce_tracking_historial` VALUES (1, 5, 2, 1, 'Pedido Confirmado', 'kvjvvkjv', NULL, NULL, NULL, 'ADMIN', NULL, NULL, '2026-01-26 00:29:37', '2026-01-25 19:29:38', '2026-01-25 19:29:38');
INSERT INTO `ecommerce_tracking_historial` VALUES (2, 5, 3, 2, 'En Preparación', 'En proceso', NULL, NULL, NULL, 'ADMIN', NULL, NULL, '2026-01-26 00:38:56', '2026-01-25 19:38:56', '2026-01-25 19:38:56');
INSERT INTO `ecommerce_tracking_historial` VALUES (3, 5, 3, NULL, 'Transportista Asignado', 'Transportista: CARLOS GUTIERRES - Guía: 00125', NULL, NULL, NULL, 'ADMIN', NULL, '{\"numero_guia\": \"00125\", \"transportista\": \"CARLOS GUTIERRES\"}', '2026-01-26 00:39:40', '2026-01-25 19:39:40', '2026-01-25 19:39:40');
INSERT INTO `ecommerce_tracking_historial` VALUES (4, 5, 9, 7, 'Entregado', 'bIEN MAS', NULL, NULL, NULL, 'ADMIN', NULL, NULL, '2026-01-26 00:40:20', '2026-01-25 19:40:20', '2026-01-25 19:40:20');
INSERT INTO `ecommerce_tracking_historial` VALUES (5, 13, 1, 1, 'Tracking Iniciado', 'Se ha iniciado el seguimiento del pedido', NULL, NULL, NULL, 'ADMIN', NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 04:44:12', '2026-01-29 04:44:12');
INSERT INTO `ecommerce_tracking_historial` VALUES (6, 13, 1, 1, 'Tracking Iniciado', 'Se ha iniciado el seguimiento del pedido', NULL, NULL, NULL, 'ADMIN', NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 04:46:27', '2026-01-29 04:46:27');

-- ----------------------------
-- Table structure for ecommerce_tracking_notificaciones
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_tracking_notificaciones`;
CREATE TABLE `ecommerce_tracking_notificaciones`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pedido_id` int NOT NULL,
  `cliente_id` int NOT NULL,
  `tipo` enum('email','push','sms','whatsapp') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'push',
  `estado_id` int NULL DEFAULT NULL,
  `titulo` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `mensaje` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `enviado` tinyint(1) NULL DEFAULT 0,
  `fecha_envio` datetime NULL DEFAULT NULL,
  `error_mensaje` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estado_id`(`estado_id` ASC) USING BTREE,
  INDEX `idx_pedido`(`pedido_id` ASC) USING BTREE,
  INDEX `idx_cliente`(`cliente_id` ASC) USING BTREE,
  INDEX `idx_enviado`(`enviado` ASC) USING BTREE,
  CONSTRAINT `ecommerce_tracking_notificaciones_ibfk_1` FOREIGN KEY (`pedido_id`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_tracking_notificaciones_ibfk_2` FOREIGN KEY (`cliente_id`) REFERENCES `ecommerce_clientes_olddddd` (`id_cliente`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_tracking_notificaciones_ibfk_3` FOREIGN KEY (`estado_id`) REFERENCES `ecommerce_tracking_estados` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_tracking_notificaciones
-- ----------------------------

-- ----------------------------
-- Table structure for ecommerce_tracking_ubicaciones
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_tracking_ubicaciones`;
CREATE TABLE `ecommerce_tracking_ubicaciones`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `id_etapa` int NULL DEFAULT NULL,
  `nombre` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ciudad` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `latitud` decimal(10, 8) NULL DEFAULT NULL,
  `longitud` decimal(11, 8) NULL DEFAULT NULL,
  `orden` int NOT NULL DEFAULT 0,
  `completado` tinyint(1) NULL DEFAULT 0,
  `fecha_llegada` datetime NULL DEFAULT NULL,
  `fecha_salida` datetime NULL DEFAULT NULL,
  `notas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `etapa_id`(`id_etapa` ASC) USING BTREE,
  INDEX `idx_pedido`(`id_pedido` ASC) USING BTREE,
  INDEX `idx_orden`(`orden` ASC) USING BTREE,
  CONSTRAINT `ecommerce_tracking_ubicaciones_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `ecommerce_pedidos` (`id_pedido`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_tracking_ubicaciones_ibfk_2` FOREIGN KEY (`id_etapa`) REFERENCES `ecommerce_tracking_etapas` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_tracking_ubicaciones
-- ----------------------------
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (1, 5, 1, 'Creado / Recibido', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 1, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 19:59:42', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (2, 5, 2, 'En Preparación / Empaquetado', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:00:56', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (3, 5, 3, 'Listo para envío', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:58:43', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (4, 5, 4, 'Transportista Asignado', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:59:00', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (5, 5, 5, 'En tránsito', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:59:11', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (6, 5, 6, 'En ciudad de destino', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:59:34', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (7, 5, 7, 'Entregado', 'CALLE CALLA 0123', 'LIMA', NULL, NULL, 0, 0, '2026-01-29 19:59:31', '2026-01-25 19:59:34', 'BIEN MAS', '2026-01-25 20:59:59', '2026-01-25 21:01:35');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (8, 13, 1, 'Almacén de Origen', NULL, NULL, NULL, NULL, 1, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (9, 13, 2, 'Centro de Acopio', NULL, NULL, NULL, NULL, 2, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (10, 13, 3, 'Hub Nacional', NULL, NULL, NULL, NULL, 3, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (11, 13, 4, 'Hub Regional', NULL, NULL, NULL, NULL, 4, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (12, 13, 5, 'Centro de Ciudad', NULL, NULL, NULL, NULL, 5, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (13, 13, 6, 'Vehículo de Reparto', NULL, NULL, NULL, NULL, 6, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (14, 13, 7, 'Dirección de Entrega', NULL, NULL, NULL, NULL, 7, 0, NULL, NULL, NULL, '2026-01-29 09:44:12', '2026-01-29 09:44:12');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (15, 13, 1, 'Almacén de Origen', NULL, NULL, NULL, NULL, 1, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (16, 13, 2, 'Centro de Acopio', NULL, NULL, NULL, NULL, 2, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (17, 13, 3, 'Hub Nacional', NULL, NULL, NULL, NULL, 3, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (18, 13, 4, 'Hub Regional', NULL, NULL, NULL, NULL, 4, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (19, 13, 5, 'Centro de Ciudad', NULL, NULL, NULL, NULL, 5, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (20, 13, 6, 'Vehículo de Reparto', NULL, NULL, NULL, NULL, 6, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');
INSERT INTO `ecommerce_tracking_ubicaciones` VALUES (21, 13, 7, 'Dirección de Entrega', NULL, NULL, NULL, NULL, 7, 0, NULL, NULL, NULL, '2026-01-29 09:46:27', '2026-01-29 09:46:27');

-- ----------------------------
-- Table structure for ecommerce_wishlist
-- ----------------------------
DROP TABLE IF EXISTS `ecommerce_wishlist`;
CREATE TABLE `ecommerce_wishlist`  (
  `id_wishlist` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `id_producto` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_wishlist`) USING BTREE,
  UNIQUE INDEX `uk_cliente_producto`(`id_cliente` ASC, `id_producto` ASC) USING BTREE,
  INDEX `id_producto`(`id_producto` ASC) USING BTREE,
  INDEX `idx_cliente`(`id_cliente` ASC) USING BTREE,
  CONSTRAINT `ecommerce_wishlist_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `ecommerce_clientes_olddddd` (`id_cliente`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ecommerce_wishlist_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `ecommerce_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ecommerce_wishlist
-- ----------------------------

-- ----------------------------
-- Table structure for failed_jobs
-- ----------------------------
DROP TABLE IF EXISTS `failed_jobs`;
CREATE TABLE `failed_jobs`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `failed_jobs_uuid_unique`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of failed_jobs
-- ----------------------------

-- ----------------------------
-- Table structure for finanzas_cotizacion
-- ----------------------------
DROP TABLE IF EXISTS `finanzas_cotizacion`;
CREATE TABLE `finanzas_cotizacion`  (
  `id_cotizacion` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Código de cotización ejemplo: COT-0204-2025',
  `version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '2.0' COMMENT 'Versión del documento',
  `proyecto` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Nombre del proyecto',
  `etapa` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Etapa del proyecto',
  `cliente` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Nombre del cliente o razón social',
  `ruc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'RUC del cliente',
  `direccion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Dirección del proyecto',
  `ciudad` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'LIMA' COMMENT 'Ciudad',
  `provincia` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'LIMA' COMMENT 'Provincia',
  `fecha` date NOT NULL COMMENT 'Fecha de emisión',
  `validez_oferta` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '3 DÍAS CALENDARIO',
  `tiempo_ejecucion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '10 DÍAS CALENDARIO',
  `forma_pago` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PRESENTADA LA FACTURA PAGO A 15 DÍAS',
  `adelanto` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '50% DE ADELANTO DEL MONTO TOTAL',
  `garantia` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `observaciones` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `costo_directo` decimal(12, 2) NULL DEFAULT 0.00 COMMENT 'Suma de todos los items',
  `gastos_generales` decimal(5, 2) NULL DEFAULT 11.00 COMMENT 'Porcentaje de gastos generales',
  `utilidad` decimal(5, 2) NULL DEFAULT 8.00 COMMENT 'Porcentaje de utilidad',
  `subtotal` decimal(12, 2) NULL DEFAULT 0.00 COMMENT 'Subtotal antes de IGV',
  `igv` decimal(5, 2) NULL DEFAULT 18.00 COMMENT 'Porcentaje de IGV',
  `total` decimal(12, 2) NULL DEFAULT 0.00 COMMENT 'Total final',
  `estado` enum('PENDIENTE','APROBADO','RECHAZADO','FACTURADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PENDIENTE',
  `id_usuario_creacion` int NULL DEFAULT NULL COMMENT 'Usuario que creó la cotización',
  `id_usuario_aprobacion` int NULL DEFAULT NULL COMMENT 'Usuario que aprobó',
  `fecha_aprobacion` datetime NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'S' COMMENT 'S=Activo, N=Inactivo',
  PRIMARY KEY (`id_cotizacion`) USING BTREE,
  INDEX `idx_codigo`(`codigo` ASC) USING BTREE,
  INDEX `idx_cliente`(`cliente` ASC) USING BTREE,
  INDEX `idx_fecha`(`fecha` ASC) USING BTREE,
  INDEX `idx_estado`(`estado` ASC) USING BTREE,
  INDEX `idx_activo`(`Activo` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Tabla de cotizaciones y presupuestos' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of finanzas_cotizacion
-- ----------------------------
INSERT INTO `finanzas_cotizacion` VALUES (1, '101', '2.0', 'erwarewr', '1', '3424234', '234234234', 'MZ3333333333 JOSE CARLOS werwerwer', 'LIMA', 'LIMA', '2025-11-28', '3 DÍAS CALENDARIO', '10 DÍAS CALENDARIO', 'PRESENTADA LA FACTURA PAGO A 15 DÍAS', '50% DE ADELANTO DEL MONTO TOTAL', 'sdfwsdfsdf', '3wr23423423424', 70.00, 10.00, 8.00, 82.60, 18.00, 97.47, 'APROBADO', 1, 1, '2026-01-29 06:21:32', '2025-11-28 01:34:58', '2026-01-29 06:21:32', 'S');
INSERT INTO `finanzas_cotizacion` VALUES (2, '23123123', '2.0', 'LADRILLOS EN CALINETE', '1', '3424234', '12312313', 'Avenida Jose Carlos Mariategui, Lima, Peru', 'LIMA', 'LIMA', '2026-01-30', '3 DÍAS CALENDARIO', '10 DÍAS CALENDARIO', 'PRESENTADA LA FACTURA PAGO A 15 DÍAS', '50% DE ADELANTO DEL MONTO TOTAL', 'WQWEQWE', 'QWEQWEQEW', 0.00, 11.00, 8.00, 0.00, 18.00, 0.00, 'PENDIENTE', 1, NULL, NULL, '2026-01-30 11:02:03', '2026-01-30 11:02:03', 'S');

-- ----------------------------
-- Table structure for finanzas_cotizacion_evidencias
-- ----------------------------
DROP TABLE IF EXISTS `finanzas_cotizacion_evidencias`;
CREATE TABLE `finanzas_cotizacion_evidencias`  (
  `id_evidencia` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NULL DEFAULT NULL,
  `id_historial` int NULL DEFAULT NULL,
  `estado_aplicado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `nombre_archivo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ruta_archivo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tipo_archivo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'image/jpeg, image/png, application/pdf',
  `tamano_archivo` int NULL DEFAULT NULL COMMENT 'Tamaño en bytes',
  `id_usuario_subida` int NULL DEFAULT NULL,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'S',
  PRIMARY KEY (`id_evidencia`) USING BTREE,
  INDEX `id_historial`(`id_historial` ASC) USING BTREE,
  INDEX `idx_cotizacion`(`id_cotizacion` ASC) USING BTREE,
  INDEX `idx_estado`(`estado_aplicado` ASC) USING BTREE,
  INDEX `idx_activo`(`Activo` ASC) USING BTREE,
  CONSTRAINT `finanzas_cotizacion_evidencias_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `finanzas_cotizacion` (`id_cotizacion`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `finanzas_cotizacion_evidencias_ibfk_2` FOREIGN KEY (`id_historial`) REFERENCES `finanzas_cotizacion_historial` (`id_historial`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of finanzas_cotizacion_evidencias
-- ----------------------------
INSERT INTO `finanzas_cotizacion_evidencias` VALUES (1, 2, NULL, NULL, 'IMPRIMIR-1.pdf', 'cotizaciones/evidencias/evidencia_2_1769797857.pdf', 'application/pdf', 1063860, 1, NULL, '2026-01-30 13:30:57', 'S');
INSERT INTO `finanzas_cotizacion_evidencias` VALUES (2, 2, NULL, NULL, 'IMPRIMIR-2.pdf', 'cotizaciones/evidencias/evidencia_2_1769800130.pdf', 'application/pdf', 1063860, 1, NULL, '2026-01-30 14:08:50', 'S');

-- ----------------------------
-- Table structure for finanzas_cotizacion_historial
-- ----------------------------
DROP TABLE IF EXISTS `finanzas_cotizacion_historial`;
CREATE TABLE `finanzas_cotizacion_historial`  (
  `id_historial` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NOT NULL,
  `estado_anterior` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `estado_nuevo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `id_usuario` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historial`) USING BTREE,
  INDEX `idx_cotizacion`(`id_cotizacion` ASC) USING BTREE,
  CONSTRAINT `finanzas_cotizacion_historial_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `finanzas_cotizacion` (`id_cotizacion`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Historial de cambios de estado de cotizaciones' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of finanzas_cotizacion_historial
-- ----------------------------
INSERT INTO `finanzas_cotizacion_historial` VALUES (1, 1, NULL, 'PENDIENTE', 'Cotización creada', 1, '2025-11-28 01:34:58');
INSERT INTO `finanzas_cotizacion_historial` VALUES (2, 1, 'PENDIENTE', 'APROBADO', 'sasdasd', 1, '2026-01-29 06:21:32');
INSERT INTO `finanzas_cotizacion_historial` VALUES (3, 2, NULL, 'PENDIENTE', 'Cotización creada', 1, '2026-01-30 11:02:03');

-- ----------------------------
-- Table structure for finanzas_cotizacion_items
-- ----------------------------
DROP TABLE IF EXISTS `finanzas_cotizacion_items`;
CREATE TABLE `finanzas_cotizacion_items`  (
  `id_cotizacion_item` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NOT NULL COMMENT 'FK a finanzas_cotizacion',
  `item` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Código del item ejemplo: 01.01.01',
  `descripcion` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Descripción del trabajo/producto',
  `unidad` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'glb' COMMENT 'Unidad de medida: glb, m2, m3, ml, und, kg',
  `cantidad` decimal(10, 2) NULL DEFAULT 1.00 COMMENT 'Cantidad',
  `precio_unitario` decimal(12, 2) NULL DEFAULT 0.00 COMMENT 'Precio unitario',
  `subtotal` decimal(12, 2) NULL DEFAULT 0.00 COMMENT 'Subtotal del item (cantidad * precio_unitario)',
  `orden` int NULL DEFAULT 0 COMMENT 'Orden de visualización',
  `seccion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Sección o grupo al que pertenece (ej: ADICIONALES)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'S',
  PRIMARY KEY (`id_cotizacion_item`) USING BTREE,
  INDEX `idx_cotizacion`(`id_cotizacion` ASC) USING BTREE,
  INDEX `idx_orden`(`orden` ASC) USING BTREE,
  CONSTRAINT `finanzas_cotizacion_items_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `finanzas_cotizacion` (`id_cotizacion`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Items de detalle de las cotizaciones' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of finanzas_cotizacion_items
-- ----------------------------
INSERT INTO `finanzas_cotizacion_items` VALUES (1, 1, '01.01.01', '34234', 'm2', 1.00, 50.00, 50.00, 1, NULL, '2025-11-28 01:34:58', '2025-11-28 03:01:44', 'N');
INSERT INTO `finanzas_cotizacion_items` VALUES (2, 1, '02.02.02', '234234234', 'ml', 1.00, 20.00, 20.00, 2, NULL, '2025-11-28 01:34:58', '2025-11-28 03:01:44', 'N');
INSERT INTO `finanzas_cotizacion_items` VALUES (3, 1, '01.01.01', '34234', 'm2', 1.00, 50.00, 50.00, 1, NULL, '2025-11-28 03:01:44', '2025-11-28 03:01:44', 'S');
INSERT INTO `finanzas_cotizacion_items` VALUES (4, 1, '02.02.02', '234234234', 'ml', 1.00, 20.00, 20.00, 2, NULL, '2025-11-28 03:01:44', '2025-11-28 03:01:44', 'S');
INSERT INTO `finanzas_cotizacion_items` VALUES (5, 1, '03', 'wqeqeqe', 'glb', 1.00, 0.00, 0.00, 3, NULL, '2025-11-28 03:01:44', '2025-11-28 03:01:44', 'S');

-- ----------------------------
-- Table structure for log_despacho_material
-- ----------------------------
DROP TABLE IF EXISTS `log_despacho_material`;
CREATE TABLE `log_despacho_material`  (
  `id_material_log` int NOT NULL AUTO_INCREMENT,
  `id_material_combobox` int NULL DEFAULT NULL,
  `nombre_material` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `peso` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_unidad_medida` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_material_categoria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `tiempos` int NULL DEFAULT NULL COMMENT '0: Significa que es un registro Anterior o el original sin cambios (Antes - Before\r\n1: Significa el nuevo registro que sufrio cambios en unos de sus campos (Despues o Affter)',
  `id_diseno` int NULL DEFAULT NULL,
  PRIMARY KEY (`id_material_log`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log_despacho_material
-- ----------------------------

-- ----------------------------
-- Table structure for migrations
-- ----------------------------
DROP TABLE IF EXISTS `migrations`;
CREATE TABLE `migrations`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of migrations
-- ----------------------------
INSERT INTO `migrations` VALUES (1, '2014_10_12_000000_create_users_table', 1);
INSERT INTO `migrations` VALUES (2, '2014_10_12_100000_create_password_reset_tokens_table', 1);
INSERT INTO `migrations` VALUES (3, '2014_10_12_100000_create_password_resets_table', 1);
INSERT INTO `migrations` VALUES (4, '2019_08_19_000000_create_failed_jobs_table', 1);
INSERT INTO `migrations` VALUES (5, '2019_12_14_000001_create_personal_access_tokens_table', 1);
INSERT INTO `migrations` VALUES (6, '2023_10_06_183711_create_product_table', 1);

-- ----------------------------
-- Table structure for password_reset_tokens
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens`  (
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of password_reset_tokens
-- ----------------------------

-- ----------------------------
-- Table structure for password_resets
-- ----------------------------
DROP TABLE IF EXISTS `password_resets`;
CREATE TABLE `password_resets`  (
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  INDEX `password_resets_email_index`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of password_resets
-- ----------------------------

-- ----------------------------
-- Table structure for personal_access_tokens
-- ----------------------------
DROP TABLE IF EXISTS `personal_access_tokens`;
CREATE TABLE `personal_access_tokens`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `tokenable_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tokenable_id` bigint UNSIGNED NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `abilities` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `personal_access_tokens_token_unique`(`token` ASC) USING BTREE,
  INDEX `personal_access_tokens_tokenable_type_tokenable_id_index`(`tokenable_type` ASC, `tokenable_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of personal_access_tokens
-- ----------------------------
INSERT INTO `personal_access_tokens` VALUES (1, 'App\\Models\\User', 1, 'API TOKEN', '47b32bc6bf0bc5bfc63ee49e94c3f475b9105a5561f70105c63cb7c959817c76', '[\"*\"]', '2024-11-03 22:10:37', NULL, '2024-11-03 22:02:43', '2024-11-03 22:10:37');
INSERT INTO `personal_access_tokens` VALUES (2, 'App\\Models\\User', 1, 'API TOKEN', '0db9531549c6c8dab7e228a67c0197d10efda7171a8a419b743e707dc4524c3c', '[\"*\"]', '2024-11-04 01:38:12', NULL, '2024-11-03 22:12:51', '2024-11-04 01:38:12');
INSERT INTO `personal_access_tokens` VALUES (3, 'App\\Models\\User', 1, 'API TOKEN', '045de95c151689a7c97bfcb3f4308c06ebf6ae348f7c80795e6163fcaaf98281', '[\"*\"]', '2024-11-04 03:29:38', NULL, '2024-11-04 01:38:19', '2024-11-04 03:29:38');
INSERT INTO `personal_access_tokens` VALUES (4, 'App\\Models\\User', 1, 'API TOKEN', '3611009d28c5571fdbb171a75749e40d44750b4013809c4990982f76b90140d7', '[\"*\"]', '2024-11-04 04:10:58', NULL, '2024-11-04 03:30:23', '2024-11-04 04:10:58');
INSERT INTO `personal_access_tokens` VALUES (5, 'App\\Models\\User', 1, 'API TOKEN', '09dd01307cb6f4f1a7cb8457c0b32ef7f16811820c247229e00b4fa611c031cf', '[\"*\"]', NULL, NULL, '2024-11-04 04:18:24', '2024-11-04 04:18:24');
INSERT INTO `personal_access_tokens` VALUES (6, 'App\\Models\\User', 1, 'API TOKEN', 'd0c9d5c6cd51fc7be81f1b98b1e56cdd351a3249e6308e01ce06903f8d1ad5da', '[\"*\"]', '2024-11-04 04:30:05', NULL, '2024-11-04 04:29:42', '2024-11-04 04:30:05');
INSERT INTO `personal_access_tokens` VALUES (7, 'App\\Models\\User', 1, 'API TOKEN', 'ef05fe284e9c82b903188f2c6321f169b6748d8e11a4a3dcf53ba64022669052', '[\"*\"]', NULL, NULL, '2024-11-04 04:32:54', '2024-11-04 04:32:54');
INSERT INTO `personal_access_tokens` VALUES (8, 'App\\Models\\User', 1, 'API TOKEN', '75383431ac0d6dd4b10b49b0c6309f12576dfdb43056e2ffecdd3e92712380a6', '[\"*\"]', NULL, NULL, '2024-11-04 04:34:00', '2024-11-04 04:34:00');
INSERT INTO `personal_access_tokens` VALUES (9, 'App\\Models\\User', 1, 'API TOKEN', '392ae4d6754b815b7f72ce3e8cb5354c561ceeb5d928fd28b0f2cf02df8e3beb', '[\"*\"]', NULL, NULL, '2024-11-04 04:37:16', '2024-11-04 04:37:16');
INSERT INTO `personal_access_tokens` VALUES (10, 'App\\Models\\User', 1, 'API TOKEN', '58e0087ca78b963567ba417ab69e80f0d2319f51c6948b0df7d8c2925d5a0227', '[\"*\"]', NULL, NULL, '2024-11-04 04:38:27', '2024-11-04 04:38:27');
INSERT INTO `personal_access_tokens` VALUES (11, 'App\\Models\\User', 1, 'API TOKEN', 'b5ab339cf22950333a76babf09984568901254aba5277fde8c0469bc7c6a8a76', '[\"*\"]', '2024-11-04 05:07:01', NULL, '2024-11-04 04:41:28', '2024-11-04 05:07:01');
INSERT INTO `personal_access_tokens` VALUES (12, 'App\\Models\\User', 1, 'API TOKEN', 'a7c1133fd809d39a1df4bdcaef9a1b5e899c086efa61734f997723146eee90d9', '[\"*\"]', NULL, NULL, '2024-12-03 05:32:59', '2024-12-03 05:32:59');
INSERT INTO `personal_access_tokens` VALUES (13, 'App\\Models\\User', 1, 'API TOKEN', '84fa0a7e71d10400b71295a0ce87474447b631ea5274abd6426721681a6b9846', '[\"*\"]', NULL, NULL, '2024-12-03 05:33:22', '2024-12-03 05:33:22');
INSERT INTO `personal_access_tokens` VALUES (14, 'App\\Models\\User', 1, 'API TOKEN', 'd2e7ff07dac6354b1e3510b4e71e2f8d05d2b08156632158bb0ecaee831b2804', '[\"*\"]', NULL, NULL, '2024-12-03 05:34:01', '2024-12-03 05:34:01');
INSERT INTO `personal_access_tokens` VALUES (15, 'App\\Models\\User', 1, 'API TOKEN', 'a3ea113943e127eedebbb2f26267d74662fd08b39fd6bab00fd5f797a655a4aa', '[\"*\"]', NULL, NULL, '2024-12-03 05:47:28', '2024-12-03 05:47:28');
INSERT INTO `personal_access_tokens` VALUES (16, 'App\\Models\\User', 1, 'API TOKEN', '4ac2451dd1010188d415aaaac40e78a1b12c000a62de6fbb99cc236b3c1863a9', '[\"*\"]', NULL, NULL, '2024-12-03 05:48:29', '2024-12-03 05:48:29');
INSERT INTO `personal_access_tokens` VALUES (17, 'App\\Models\\User', 1, 'API TOKEN', '6e5a5ac20e7cf17f9207d8a7a8acfa0c5ae7177024e9be575914ef8f6fd78dff', '[\"*\"]', NULL, NULL, '2024-12-03 05:49:37', '2024-12-03 05:49:37');
INSERT INTO `personal_access_tokens` VALUES (18, 'App\\Models\\User', 1, 'API TOKEN', 'e85d54ddc2199f78a4dc97b116d088e4f92bc19601ae431365fe3bee2f909a4a', '[\"*\"]', NULL, NULL, '2024-12-03 05:52:35', '2024-12-03 05:52:35');
INSERT INTO `personal_access_tokens` VALUES (19, 'App\\Models\\User', 1, 'API TOKEN', 'e04a731185636afd29d4d5f2097c1e07b25cc4b11084abd7f8e293432e781cff', '[\"*\"]', NULL, NULL, '2024-12-03 05:58:53', '2024-12-03 05:58:53');
INSERT INTO `personal_access_tokens` VALUES (20, 'App\\Models\\User', 1, 'API TOKEN', 'b47f5ede1475a3089bef1303ecba3b0741df63c4051f3a7210c112d8693b172e', '[\"*\"]', NULL, NULL, '2024-12-03 05:59:33', '2024-12-03 05:59:33');
INSERT INTO `personal_access_tokens` VALUES (21, 'App\\Models\\User', 2, 'API TOKEN', 'ca14cb58705fddf3f003d1912c64471a347b60882d68dea7bffe289b2dbffbbf', '[\"*\"]', NULL, NULL, '2024-12-03 05:59:51', '2024-12-03 05:59:51');
INSERT INTO `personal_access_tokens` VALUES (22, 'App\\Models\\User', 1, 'API TOKEN', 'addb96c3b4bbdae7f76de7bc0917ef4a5fb122481eff9aa0c7af686f1127f5cb', '[\"*\"]', NULL, NULL, '2024-12-03 06:00:18', '2024-12-03 06:00:18');
INSERT INTO `personal_access_tokens` VALUES (23, 'App\\Models\\User', 1, 'API TOKEN', 'be86f2e26a0a567912ded957dfe7186d5b264d9bffcf8513ddb032776ff5f879', '[\"*\"]', NULL, NULL, '2024-12-03 06:00:19', '2024-12-03 06:00:19');
INSERT INTO `personal_access_tokens` VALUES (24, 'App\\Models\\User', 1, 'API TOKEN', 'a00085b89d7e96820679bf9c8fb8db0dacf749b3e4f4b897f0ab31cdbd634778', '[\"*\"]', NULL, NULL, '2024-12-03 06:01:09', '2024-12-03 06:01:09');
INSERT INTO `personal_access_tokens` VALUES (25, 'App\\Models\\User', 1, 'API TOKEN', 'f0bc49d13b21cc4bf9a90ddc9ece8be8777027bc0a68da4435b622147e4a0a88', '[\"*\"]', NULL, NULL, '2024-12-03 06:02:53', '2024-12-03 06:02:53');
INSERT INTO `personal_access_tokens` VALUES (26, 'App\\Models\\User', 1, 'API TOKEN', '268658f492385e412bacd2c9d7448da249b91b9760cce34ac47649aaee9120ef', '[\"*\"]', NULL, NULL, '2024-12-03 06:07:23', '2024-12-03 06:07:23');
INSERT INTO `personal_access_tokens` VALUES (27, 'App\\Models\\User', 1, 'API TOKEN', '4af16adbc358988dd76f414d0780b2e5cb2947fda410f337bc69d9121d7190d2', '[\"*\"]', NULL, NULL, '2024-12-03 06:07:52', '2024-12-03 06:07:52');
INSERT INTO `personal_access_tokens` VALUES (28, 'App\\Models\\User', 1, 'API TOKEN', '06aa95325f56fb038c52c5637149ece6bbdd23cdbb9591223fbc277219ddd0f1', '[\"*\"]', NULL, NULL, '2024-12-04 03:41:16', '2024-12-04 03:41:16');
INSERT INTO `personal_access_tokens` VALUES (29, 'App\\Models\\User', 1, 'API TOKEN', '22f34ccc16bfbb10ae7980f1e762604a98e95ff791b3c8d129830e7537bbbe28', '[\"*\"]', NULL, NULL, '2024-12-04 03:44:14', '2024-12-04 03:44:14');
INSERT INTO `personal_access_tokens` VALUES (30, 'App\\Models\\User', 1, 'API TOKEN', '9c1ebf7c66de0bb2c276d6a0c6d9bd8fd9cb1fa649fef110f0fce1ddd229f650', '[\"*\"]', NULL, NULL, '2024-12-04 04:33:34', '2024-12-04 04:33:34');
INSERT INTO `personal_access_tokens` VALUES (31, 'App\\Models\\User', 1, 'API TOKEN', '1dc2b00a71f77df7045d7c4e30a15fa61bbc78927c7efb7ad9d896ca56a80b82', '[\"*\"]', NULL, NULL, '2024-12-04 05:28:28', '2024-12-04 05:28:28');
INSERT INTO `personal_access_tokens` VALUES (32, 'App\\Models\\User', 1, 'API TOKEN', 'a270ab11d93806a0299a0b7e04cfbc0cc1d84cee7fc813b952ffd6324d9a8fde', '[\"*\"]', NULL, NULL, '2024-12-04 05:34:09', '2024-12-04 05:34:09');
INSERT INTO `personal_access_tokens` VALUES (33, 'App\\Models\\User', 1, 'API TOKEN', '1a4fa5a48cf280dd9434d9d60fb15b7d7b4c689911cbbf056ac9718bbcc52bb2', '[\"*\"]', NULL, NULL, '2024-12-04 05:34:13', '2024-12-04 05:34:13');
INSERT INTO `personal_access_tokens` VALUES (34, 'App\\Models\\User', 1, 'API TOKEN', '2305890d6485a8ca1f1b7334dc6ee03941a809778264fe6b7ed7557927ce4e9a', '[\"*\"]', NULL, NULL, '2024-12-04 06:03:46', '2024-12-04 06:03:46');
INSERT INTO `personal_access_tokens` VALUES (35, 'App\\Models\\User', 1, 'API TOKEN', '64d224409f48a6452b85f1314daf1e2b76c8868f1ecd829b144110574ea0899e', '[\"*\"]', NULL, NULL, '2024-12-13 09:20:11', '2024-12-13 09:20:11');
INSERT INTO `personal_access_tokens` VALUES (36, 'App\\Models\\User', 1, 'API TOKEN', 'd0bb184fdcbbfe5382ebdbf5486874b01a0d889f19aac07bac6417a104d731d5', '[\"*\"]', NULL, NULL, '2024-12-13 09:21:18', '2024-12-13 09:21:18');
INSERT INTO `personal_access_tokens` VALUES (37, 'App\\Models\\User', 1, 'API TOKEN', 'd54e4f5a5179344399f7049fc4da3e050adeb0dbfbb8450546fac426ccc2486d', '[\"*\"]', NULL, NULL, '2024-12-13 09:28:00', '2024-12-13 09:28:00');
INSERT INTO `personal_access_tokens` VALUES (38, 'App\\Models\\User', 1, 'API TOKEN', 'd50083dc1d957f7fe2b4cc4e3e2b5e3ddd635653073fb359a9da780f3d250aa3', '[\"*\"]', NULL, NULL, '2024-12-16 22:49:08', '2024-12-16 22:49:08');
INSERT INTO `personal_access_tokens` VALUES (39, 'App\\Models\\User', 1, 'API TOKEN', 'ae18bdb2299b4d6eb0944d13492e00478b17f554356b21d3beb2748ec7b8e091', '[\"*\"]', NULL, NULL, '2024-12-16 23:23:09', '2024-12-16 23:23:09');
INSERT INTO `personal_access_tokens` VALUES (40, 'App\\Models\\User', 1, 'API TOKEN', 'bb14ca4ae9cc7f471b381e98939d4ffb6fb253d68d266bcd661955f83679e415', '[\"*\"]', NULL, NULL, '2024-12-16 23:27:40', '2024-12-16 23:27:40');
INSERT INTO `personal_access_tokens` VALUES (41, 'App\\Models\\User', 1, 'API TOKEN', '36eded0bb4024a6bef59f22f7a266b78c14ef5b600536c11cb0cf7a94d3cfc5c', '[\"*\"]', NULL, NULL, '2024-12-17 04:14:59', '2024-12-17 04:14:59');
INSERT INTO `personal_access_tokens` VALUES (42, 'App\\Models\\User', 1, 'API TOKEN', 'fb4fbc55d5bf2cf6a5393b94c0aeee3c7a076e02011996b0cf803d4d6c656fd7', '[\"*\"]', NULL, NULL, '2024-12-17 04:16:23', '2024-12-17 04:16:23');
INSERT INTO `personal_access_tokens` VALUES (43, 'App\\Models\\User', 1, 'API TOKEN', '42771436d6ca0a525994b9fcb55fb5a1dba0a15eaf4e2f37aab027a5e4e22579', '[\"*\"]', NULL, NULL, '2024-12-17 04:19:13', '2024-12-17 04:19:13');
INSERT INTO `personal_access_tokens` VALUES (44, 'App\\Models\\User', 1, 'API TOKEN', '9c202986fa44a4fc42763ee57b18ba6d118f3d940babe07b9dce1e84ac44e351', '[\"*\"]', NULL, NULL, '2024-12-17 04:21:32', '2024-12-17 04:21:32');
INSERT INTO `personal_access_tokens` VALUES (45, 'App\\Models\\User', 1, 'API TOKEN', 'c8599256523e7110e780a2e6bdb6b9388cdedef86dbc8e03c04c321a458bbb80', '[\"*\"]', NULL, NULL, '2024-12-17 04:25:14', '2024-12-17 04:25:14');
INSERT INTO `personal_access_tokens` VALUES (46, 'App\\Models\\User', 1, 'API TOKEN', '02b08c34798f7f7da17e0d9b3b3a5627d975c8a73edd7001b016748697537982', '[\"*\"]', NULL, NULL, '2024-12-23 20:58:33', '2024-12-23 20:58:33');
INSERT INTO `personal_access_tokens` VALUES (47, 'App\\Models\\User', 1, 'API TOKEN', 'c4ed5929c456b37b37aa39548b19b70fbc36acb7752daf1c7eea1ff3b2e69872', '[\"*\"]', NULL, NULL, '2025-01-04 00:54:09', '2025-01-04 00:54:09');
INSERT INTO `personal_access_tokens` VALUES (48, 'App\\Models\\User', 1, 'API TOKEN', 'fc914782a6596448baabbb5b5f19c08dee51b38e18eab416abb89aba00b4f75f', '[\"*\"]', '2025-01-08 06:10:45', NULL, '2025-01-08 06:10:30', '2025-01-08 06:10:45');
INSERT INTO `personal_access_tokens` VALUES (49, 'App\\Models\\User', 1, 'API TOKEN', '11e55ad2ccea015c9a1244967eda80fe635904c531840f2304c5808385cda221', '[\"*\"]', NULL, NULL, '2025-01-08 06:13:44', '2025-01-08 06:13:44');
INSERT INTO `personal_access_tokens` VALUES (50, 'App\\Models\\User', 1, 'API TOKEN', 'f57d49658fdea16defd808d82d0fce39d31713e895f38f47415072040da54d36', '[\"*\"]', NULL, NULL, '2025-01-08 06:22:08', '2025-01-08 06:22:08');
INSERT INTO `personal_access_tokens` VALUES (51, 'App\\Models\\User', 1, 'API TOKEN', '882e7bc63fe4bf7071c65d4e5337f15a1a3d2cbe5ba2430fcb3d3d39aa89c1b3', '[\"*\"]', NULL, NULL, '2025-01-08 06:29:22', '2025-01-08 06:29:22');
INSERT INTO `personal_access_tokens` VALUES (52, 'App\\Models\\User', 1, 'API TOKEN', '7060dcd6d067019d800d8a32dc2daf89031a4c08ec0abad6d6a5a81053e8a1e7', '[\"*\"]', NULL, NULL, '2025-01-08 06:35:49', '2025-01-08 06:35:49');
INSERT INTO `personal_access_tokens` VALUES (53, 'App\\Models\\User', 1, 'API TOKEN', '4ee4a507817ead03c58dc958d5f4323c6b18956499c99114d76c327f9ba61e0e', '[\"*\"]', NULL, NULL, '2025-01-08 06:38:15', '2025-01-08 06:38:15');
INSERT INTO `personal_access_tokens` VALUES (54, 'App\\Models\\User', 1, 'API TOKEN', 'baaa5b6ffe62b43ba3d5d1c0ea8ed9673d293f26e302773c3eba64df5307d948', '[\"*\"]', NULL, NULL, '2025-01-08 06:38:58', '2025-01-08 06:38:58');
INSERT INTO `personal_access_tokens` VALUES (55, 'App\\Models\\User', 1, 'API TOKEN', '7aef1fdc670db831a03a8e9b6ee62cb4516e84fdbc94ce5a9b319f4b1b8a277d', '[\"*\"]', NULL, NULL, '2025-01-08 06:39:10', '2025-01-08 06:39:10');
INSERT INTO `personal_access_tokens` VALUES (56, 'App\\Models\\User', 1, 'API TOKEN', '28026694ea167fa4eecad30d8571acc3be36cae77df3b84f05a45a4791cefc9c', '[\"*\"]', NULL, NULL, '2025-01-08 06:43:08', '2025-01-08 06:43:08');
INSERT INTO `personal_access_tokens` VALUES (57, 'App\\Models\\User', 1, 'API TOKEN', '9fc495a3b3796a63d892cce03b80766816564fe9e4c7708c1f9c7cf90152e14a', '[\"*\"]', NULL, NULL, '2025-01-08 06:48:07', '2025-01-08 06:48:07');
INSERT INTO `personal_access_tokens` VALUES (58, 'App\\Models\\User', 1, 'API TOKEN', 'f21be5819fa8f0e815d92a8df4150e070c23455a308d9ed50c02c2a6f8fcc23a', '[\"*\"]', NULL, NULL, '2025-01-08 07:00:58', '2025-01-08 07:00:58');
INSERT INTO `personal_access_tokens` VALUES (59, 'App\\Models\\User', 1, 'API TOKEN', '3b1440a4c2a7f57c2c0d9dd3946b5d6f37c40e7d5f6fdc2de927496031aea79c', '[\"*\"]', NULL, NULL, '2025-01-08 07:32:53', '2025-01-08 07:32:53');
INSERT INTO `personal_access_tokens` VALUES (60, 'App\\Models\\User', 1, 'API TOKEN', 'a80c445e670250d5852e43dcc25eac998ac4b4f5eadeefc76bc47e9f784064cb', '[\"*\"]', NULL, NULL, '2025-01-08 22:25:06', '2025-01-08 22:25:06');
INSERT INTO `personal_access_tokens` VALUES (61, 'App\\Models\\User', 1, 'API TOKEN', '76aef234a56f8c478ec39593519313ab8721eec83a354573b9f202bbe60dbc87', '[\"*\"]', NULL, NULL, '2025-01-08 23:26:45', '2025-01-08 23:26:45');
INSERT INTO `personal_access_tokens` VALUES (62, 'App\\Models\\User', 1, 'API TOKEN', 'c4e0bb1b2e17d9c7e515390aae2dc199c444f5b90faf5dc502fc305d5a89cd61', '[\"*\"]', NULL, NULL, '2025-01-09 23:47:06', '2025-01-09 23:47:06');
INSERT INTO `personal_access_tokens` VALUES (63, 'App\\Models\\User', 1, 'API TOKEN', '3be9682099916dabae9efd59158b3daeeec2f6a6c574dd667bd8a9741acb8e6d', '[\"*\"]', NULL, NULL, '2025-01-10 00:18:21', '2025-01-10 00:18:21');
INSERT INTO `personal_access_tokens` VALUES (64, 'App\\Models\\User', 1, 'API TOKEN', '67465821761f81c32370af58f4cb48924579f81ea653b0ced58baa04fcdee4fe', '[\"*\"]', NULL, NULL, '2025-01-10 01:23:37', '2025-01-10 01:23:37');
INSERT INTO `personal_access_tokens` VALUES (65, 'App\\Models\\User', 1, 'API TOKEN', '531091a79e6d0c9f3846692890020a4d8df0008077316e08b6ace584ce0f12d9', '[\"*\"]', NULL, NULL, '2025-01-10 02:09:25', '2025-01-10 02:09:25');
INSERT INTO `personal_access_tokens` VALUES (66, 'App\\Models\\User', 1, 'API TOKEN', '6a56ca28179e65c48eb02d578ab5a1f597218bae79f0d6a1a1cc5e3ef48de413', '[\"*\"]', NULL, NULL, '2025-01-10 02:47:28', '2025-01-10 02:47:28');
INSERT INTO `personal_access_tokens` VALUES (67, 'App\\Models\\User', 1, 'API TOKEN', '1b402e80e79f53b53291ba472ab8022622388cb2869df9e3fe9d3394edfbed99', '[\"*\"]', NULL, NULL, '2025-01-10 02:48:06', '2025-01-10 02:48:06');
INSERT INTO `personal_access_tokens` VALUES (68, 'App\\Models\\User', 1, 'API TOKEN', '4d01153f47496791c2ac4fdc76f1f5a08e46acb88d303b6ac0cdcdd8175dfcdc', '[\"*\"]', NULL, NULL, '2025-01-10 02:48:19', '2025-01-10 02:48:19');
INSERT INTO `personal_access_tokens` VALUES (69, 'App\\Models\\User', 1, 'API TOKEN', '4c9d47a4d41667caa1cffae4f1364e520b115b8cd927946b249f1acf431c21e3', '[\"*\"]', NULL, NULL, '2025-01-10 02:49:34', '2025-01-10 02:49:34');
INSERT INTO `personal_access_tokens` VALUES (70, 'App\\Models\\User', 1, 'API TOKEN', '7748b2a2f542f4a91c9c59dda6947b481a2a8ca44e6ea825734b8c643df8e2eb', '[\"*\"]', NULL, NULL, '2025-01-10 02:52:26', '2025-01-10 02:52:26');
INSERT INTO `personal_access_tokens` VALUES (71, 'App\\Models\\User', 1, 'API TOKEN', '3a57a8fb3f81a9a0b035ab766a47c87a69214f167fc5a13905ac2d12040f16ac', '[\"*\"]', NULL, NULL, '2025-01-10 02:53:07', '2025-01-10 02:53:07');
INSERT INTO `personal_access_tokens` VALUES (72, 'App\\Models\\User', 1, 'API TOKEN', 'c563f7dd4490757814e9f1a2ee9b14f591c8f2819b15a298fb247d0e74e4a61c', '[\"*\"]', NULL, NULL, '2025-01-10 03:00:43', '2025-01-10 03:00:43');
INSERT INTO `personal_access_tokens` VALUES (73, 'App\\Models\\User', 1, 'API TOKEN', '5b80c249bc218f4236535d511f3c1118c4cfe849dfa42e3e502b31319ab8f41a', '[\"*\"]', NULL, NULL, '2025-01-10 04:27:18', '2025-01-10 04:27:18');

-- ----------------------------
-- Table structure for seguridad_perfil
-- ----------------------------
DROP TABLE IF EXISTS `seguridad_perfil`;
CREATE TABLE `seguridad_perfil`  (
  `id_perfil` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_perfil`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seguridad_perfil
-- ----------------------------
INSERT INTO `seguridad_perfil` VALUES (1, 'ADMINISTRADOR', '2025-01-16 11:31:29', '2025-03-15 01:01:24', 'S');
INSERT INTO `seguridad_perfil` VALUES (2, 'OPERACIONES', '2025-01-16 11:31:29', '2025-11-23 19:29:04', 'N');
INSERT INTO `seguridad_perfil` VALUES (16, 'ADMINISTRATIVO', '2025-02-11 16:17:37', '2025-11-20 07:31:54', 'S');
INSERT INTO `seguridad_perfil` VALUES (17, 'OPERADOR PLANTA', '2025-02-13 16:32:00', '2025-11-20 07:32:09', 'N');
INSERT INTO `seguridad_perfil` VALUES (18, 'CALIDAD', '2025-02-13 16:46:09', '2025-11-20 07:32:19', 'N');

-- ----------------------------
-- Table structure for seguridad_perfil_users
-- ----------------------------
DROP TABLE IF EXISTS `seguridad_perfil_users`;
CREATE TABLE `seguridad_perfil_users`  (
  `id_perfil_users` int NOT NULL AUTO_INCREMENT,
  `id_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_perfil` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_perfil_users`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seguridad_perfil_users
-- ----------------------------
INSERT INTO `seguridad_perfil_users` VALUES (39, '13', '1', '2025-01-27 18:03:14');
INSERT INTO `seguridad_perfil_users` VALUES (42, '6', '2', '2025-02-04 00:22:59');
INSERT INTO `seguridad_perfil_users` VALUES (43, '24', '16', '2025-02-11 11:21:29');
INSERT INTO `seguridad_perfil_users` VALUES (44, '25', '17', '2025-02-13 11:34:50');
INSERT INTO `seguridad_perfil_users` VALUES (45, '26', '18', '2025-02-13 11:55:13');
INSERT INTO `seguridad_perfil_users` VALUES (46, '27', '2', '2025-02-20 12:43:58');
INSERT INTO `seguridad_perfil_users` VALUES (50, '30', '2', '2025-03-18 10:35:47');
INSERT INTO `seguridad_perfil_users` VALUES (51, '31', '17', '2025-03-18 10:35:56');
INSERT INTO `seguridad_perfil_users` VALUES (52, '32', '18', '2025-03-18 10:36:04');
INSERT INTO `seguridad_perfil_users` VALUES (55, '29', '16', '2025-07-01 19:25:34');
INSERT INTO `seguridad_perfil_users` VALUES (56, '33', '16', '2025-07-01 19:31:02');
INSERT INTO `seguridad_perfil_users` VALUES (57, '28', '16', '2025-07-02 08:57:51');
INSERT INTO `seguridad_perfil_users` VALUES (74, '1', '1', NULL);
INSERT INTO `seguridad_perfil_users` VALUES (75, '35', '2', '2025-11-20 03:51:31');
INSERT INTO `seguridad_perfil_users` VALUES (76, '36', '20', '2025-11-23 09:05:05');
INSERT INTO `seguridad_perfil_users` VALUES (77, '37', '19', '2025-11-23 09:05:23');
INSERT INTO `seguridad_perfil_users` VALUES (85, '34', '16', '2025-11-23 14:44:00');

-- ----------------------------
-- Table structure for seguridad_roles
-- ----------------------------
DROP TABLE IF EXISTS `seguridad_roles`;
CREATE TABLE `seguridad_roles`  (
  `id_roles` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_roles`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seguridad_roles
-- ----------------------------
INSERT INTO `seguridad_roles` VALUES (1, 'ACCESO GENERAL', '2025-01-15 16:51:11', '2025-02-19 01:01:38', 'S');
INSERT INTO `seguridad_roles` VALUES (6, 'OPERACIONES', '2025-01-15 16:51:11', '2025-02-20 17:40:00', 'S');
INSERT INTO `seguridad_roles` VALUES (7, 'SOPORTE TÉCNICO', '2025-01-15 16:51:11', '2025-02-19 01:01:22', 'S');
INSERT INTO `seguridad_roles` VALUES (16, 'ADMINISTRATIVO', '2025-02-11 16:09:21', '2025-11-20 07:59:06', 'S');
INSERT INTO `seguridad_roles` VALUES (17, 'DESPACHADOR', '2025-02-13 16:29:53', '2025-02-13 16:29:53', 'S');
INSERT INTO `seguridad_roles` VALUES (18, 'CALIDAD', '2025-02-13 16:45:59', '2025-02-13 16:45:59', 'S');

-- ----------------------------
-- Table structure for seguridad_roles_menu
-- ----------------------------
DROP TABLE IF EXISTS `seguridad_roles_menu`;
CREATE TABLE `seguridad_roles_menu`  (
  `id_roles` int NULL DEFAULT NULL,
  `id_menu` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seguridad_roles_menu
-- ----------------------------
INSERT INTO `seguridad_roles_menu` VALUES (16, 12, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 4, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 15, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 16, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 17, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 18, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 19, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1223, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1224, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1225, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1226, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1227, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1228, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1229, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1230, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1231, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1232, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1233, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1523, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1524, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1525, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1526, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1527, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1528, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1529, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1530, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1531, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1532, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1533, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1623, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1624, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1625, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1626, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1627, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1628, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1629, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1630, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1631, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1632, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1633, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1723, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1724, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1725, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1726, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1727, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1728, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1729, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1730, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1731, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1732, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1733, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1823, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1824, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1825, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1826, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1827, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1828, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1829, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1830, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1831, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1832, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1833, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1923, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1924, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1925, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1926, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1927, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1928, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1929, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1930, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1931, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1932, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (16, 1933, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (17, 1325, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (17, 1326, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (17, 1327, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (17, 1328, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1423, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1424, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1425, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1426, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1427, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1428, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1429, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (18, 1433, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 12, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 13, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 14, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 4, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 5, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 15, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 16, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 17, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 18, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 19, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 20, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 21, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 22, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1223, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1224, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1225, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1226, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1227, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1228, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1233, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1323, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1324, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1325, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1326, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1327, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1328, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1423, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1424, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1425, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1426, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1427, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1428, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1429, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1523, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1524, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1525, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1526, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1527, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1528, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1623, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1624, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1625, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1626, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1627, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1628, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1723, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1724, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1725, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1726, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1727, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1728, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1823, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1824, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1825, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1826, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1827, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1828, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1923, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1924, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1925, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1926, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1927, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 1928, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2023, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2024, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2025, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2026, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2027, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2028, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2030, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2123, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2124, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2125, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2126, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2127, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2128, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2131, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2223, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2224, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2225, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2226, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2227, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2228, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 2232, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 34, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3423, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3424, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3425, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3426, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3427, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (1, 3428, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 12, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 4, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 15, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 16, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 17, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 18, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 19, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 34, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1223, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1224, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1225, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1226, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1227, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1228, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1233, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1325, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1425, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1523, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1524, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1525, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1526, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1527, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1528, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1623, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1624, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1625, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1626, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1627, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1628, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1723, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1724, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1725, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1726, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1727, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1728, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1823, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1824, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1825, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1826, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1827, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1828, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1923, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1924, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1925, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1926, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1927, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 1928, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3423, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3424, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3425, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3426, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3427, NULL);
INSERT INTO `seguridad_roles_menu` VALUES (6, 3428, NULL);

-- ----------------------------
-- Table structure for seguridad_roles_perfil
-- ----------------------------
DROP TABLE IF EXISTS `seguridad_roles_perfil`;
CREATE TABLE `seguridad_roles_perfil`  (
  `id_roles_perfil` int NOT NULL AUTO_INCREMENT,
  `id_perfil` int NULL DEFAULT NULL,
  `id_roles` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_roles_perfil`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seguridad_roles_perfil
-- ----------------------------
INSERT INTO `seguridad_roles_perfil` VALUES (1, 1, 1, '2025-02-04 00:36:06');
INSERT INTO `seguridad_roles_perfil` VALUES (6, 17, 17, '2025-02-13 11:32:14');
INSERT INTO `seguridad_roles_perfil` VALUES (8, 2, 6, '2025-02-20 12:43:41');
INSERT INTO `seguridad_roles_perfil` VALUES (22, 18, 18, '2025-11-20 04:54:56');
INSERT INTO `seguridad_roles_perfil` VALUES (23, 19, 19, '2025-11-23 09:00:31');
INSERT INTO `seguridad_roles_perfil` VALUES (24, 20, 20, '2025-11-23 09:01:01');
INSERT INTO `seguridad_roles_perfil` VALUES (27, 16, 16, '2025-11-23 13:13:59');

-- ----------------------------
-- Table structure for sistema_menu
-- ----------------------------
DROP TABLE IF EXISTS `sistema_menu`;
CREATE TABLE `sistema_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_menu` int NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ruta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icono` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tipo` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_padre` int NULL DEFAULT NULL,
  `orden` int NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `icon_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `objetos_asignados` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sistema_menu
-- ----------------------------
INSERT INTO `sistema_menu` VALUES (5, 4, 'Administración', NULL, 'settings', 'parent', NULL, 3, '2025-06-03 06:24:55', '2025-06-23 20:04:07', NULL, '[]', 'S');
INSERT INTO `sistema_menu` VALUES (6, 19, 'Compañia', '/razon_social/index', NULL, 'submenu', 4, 1, '2025-06-03 06:24:55', '2025-06-30 05:57:16', 'CO', '[23,24,25,28,27]', 'S');
INSERT INTO `sistema_menu` VALUES (13, 5, 'Seguridad', NULL, 'security', 'parent', NULL, 1, '2025-06-03 06:24:55', '2025-11-18 22:59:56', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (14, 26, 'Personal', NULL, 'people', 'parent', 5, 2, '2025-06-03 06:24:55', '2025-11-18 21:46:53', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (15, 27, 'Clientes', '/clientes/index', NULL, 'submenu', 26, 1, '2025-06-03 06:24:55', '2026-01-27 00:37:48', 'CL', '[23,24,25,27,28]', 'S');
INSERT INTO `sistema_menu` VALUES (16, 28, 'Empleados', '/empleados/index', NULL, 'submenu', 26, 2, '2025-06-03 06:24:55', '2025-06-30 06:01:35', 'EM', '[23,24,28,25,27]', 'S');
INSERT INTO `sistema_menu` VALUES (17, 29, 'Vendedor', '/vendedor/index', NULL, 'submenu', 26, 3, '2025-06-03 06:24:55', '2025-06-30 06:02:17', 'VE', '[23,24,25,27,28]', 'S');
INSERT INTO `sistema_menu` VALUES (18, 30, 'Usuarios', '/usuarios/index', NULL, 'submenu', 5, 3, '2025-06-03 06:24:55', '2025-11-18 21:46:50', 'US', '[23,25,24,28,27,32,30]', 'S');
INSERT INTO `sistema_menu` VALUES (19, 31, 'Proveedores', '/proveedor/index', NULL, 'submenu', 26, 3, '2025-06-03 06:24:55', '2025-06-30 06:02:38', 'PR', '[23,24,25,28,27]', 'S');
INSERT INTO `sistema_menu` VALUES (20, 32, 'Accesos', '/perfiles/index', 'lock', 'submenu', NULL, 2, '2025-06-03 06:24:55', '2025-11-18 23:00:05', NULL, '[23,24,25,27,28,30,32]', 'S');
INSERT INTO `sistema_menu` VALUES (38, 38, 'Paginas', '/admin_pagina/index', NULL, 'submenu', 4, 2, '2025-11-18 21:50:31', '2025-11-18 21:54:08', 'PA', '[23,24,25,27,28]', 'S');
INSERT INTO `sistema_menu` VALUES (39, 39, 'Objetos', '/admin_pagina_objetos/index', NULL, 'submenu', 4, 3, '2025-11-18 21:51:44', '2025-11-18 22:33:19', 'OB', '[23,24,25,27,28]', 'S');
INSERT INTO `sistema_menu` VALUES (40, 40, 'Cotizador', '/cotizaciones/index', 'widgets', 'parent', NULL, 4, '2025-11-18 22:51:21', '2025-11-28 06:51:43', NULL, '[23,24,25,27,28,37]', 'S');
INSERT INTO `sistema_menu` VALUES (45, 41, 'Marcas', '/ecommerce/marcas/index', 'dashboard', 'parent', NULL, 5, '2026-01-28 06:26:08', '2026-01-28 02:45:18', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (46, 42, 'Categoria', '/ecommerce/categorias/index', 'dashboard', 'parent', NULL, 6, '2026-01-28 07:26:24', '2026-01-28 07:26:24', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (47, 43, 'Pedidos', '/ecommerce/pedidos/index', 'dashboard', 'parent', NULL, 7, '2026-01-28 07:44:16', '2026-01-28 07:44:16', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (48, 44, 'Productos', '/ecommerce/productos/index', 'dashboard', 'parent', NULL, 8, '2026-01-28 07:47:49', '2026-01-28 07:47:49', NULL, NULL, 'S');
INSERT INTO `sistema_menu` VALUES (49, 45, 'Tracking', '/ecommerce/tracking/index', 'dashboard', 'parent', NULL, 9, '2026-01-28 07:48:27', '2026-01-28 07:48:27', NULL, NULL, 'S');

-- ----------------------------
-- Table structure for sistema_menu_objetos
-- ----------------------------
DROP TABLE IF EXISTS `sistema_menu_objetos`;
CREATE TABLE `sistema_menu_objetos`  (
  `id_menu_objetos` int NOT NULL AUTO_INCREMENT,
  `id_menu` int NULL DEFAULT NULL,
  `id_objetos` int NULL DEFAULT NULL,
  `selected` bit(1) NULL DEFAULT NULL,
  `orden` int NULL DEFAULT NULL,
  `estado` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_menu_objetos`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 312 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sistema_menu_objetos
-- ----------------------------
INSERT INTO `sistema_menu_objetos` VALUES (12, 13, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (13, 13, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (14, 13, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (15, 13, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (16, 13, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (17, 13, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (23, 14, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (24, 14, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (25, 14, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (26, 14, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (27, 14, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (28, 14, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (29, 14, 29, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (34, 15, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (35, 15, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (36, 15, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (37, 15, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (38, 15, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (39, 15, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (45, 16, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (46, 16, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (47, 16, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (48, 16, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (49, 16, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (50, 16, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (56, 17, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (57, 17, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (58, 17, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (59, 17, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (60, 17, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (61, 17, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (67, 18, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (68, 18, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (69, 18, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (70, 18, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (71, 18, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (72, 18, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (100, 21, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (101, 21, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (102, 21, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (103, 21, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (104, 21, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (105, 21, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (108, 21, 31, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (111, 22, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (112, 22, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (113, 22, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (114, 22, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (115, 22, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (116, 22, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (120, 22, 32, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (155, 34, 23, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (156, 34, 24, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (157, 34, 25, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (158, 34, 26, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (159, 34, 27, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (160, 34, 28, NULL, NULL, NULL, NULL, NULL, 'S');
INSERT INTO `sistema_menu_objetos` VALUES (184, 12, 23, NULL, NULL, NULL, '2025-06-22 23:50:25', '2025-06-22 23:50:25', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (185, 12, 24, NULL, NULL, NULL, '2025-06-22 23:50:25', '2025-06-22 23:50:25', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (186, 12, 25, NULL, NULL, NULL, '2025-06-23 00:06:25', '2025-06-23 00:06:25', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (187, 12, 26, NULL, NULL, NULL, '2025-06-23 00:06:25', '2025-06-23 00:06:25', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (188, 12, 27, NULL, NULL, NULL, '2025-06-23 00:14:48', '2025-06-23 00:14:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (189, 12, 28, NULL, NULL, NULL, '2025-06-23 00:14:48', '2025-06-23 00:14:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (190, 12, 33, NULL, NULL, NULL, '2025-06-23 00:14:48', '2025-06-23 00:14:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (191, 12, 36, NULL, NULL, NULL, '2025-06-23 00:14:48', '2025-06-23 00:14:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (192, 33, 23, NULL, NULL, NULL, '2025-06-23 20:02:55', '2025-06-23 20:02:55', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (193, 33, 24, NULL, NULL, NULL, '2025-06-23 20:02:55', '2025-06-23 20:02:55', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (194, 33, 25, NULL, NULL, NULL, '2025-06-23 20:02:55', '2025-06-23 20:02:55', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (195, 33, 28, NULL, NULL, NULL, '2025-06-23 20:02:55', '2025-06-23 20:02:55', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (196, 33, 27, NULL, NULL, NULL, '2025-06-23 20:02:55', '2025-06-23 20:02:55', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (197, 35, 23, NULL, NULL, NULL, '2025-06-23 20:57:20', '2025-06-23 20:57:20', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (198, 35, 24, NULL, NULL, NULL, '2025-06-23 20:57:20', '2025-06-23 20:57:20', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (199, 35, 28, NULL, NULL, NULL, '2025-06-23 20:57:20', '2025-06-23 20:57:20', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (200, 35, 27, NULL, NULL, NULL, '2025-06-23 20:57:20', '2025-06-23 20:57:20', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (201, 33, 37, NULL, NULL, NULL, '2025-06-23 21:03:07', '2025-06-23 21:03:07', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (202, 35, 37, NULL, NULL, NULL, '2025-06-23 22:50:30', '2025-06-23 22:50:30', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (203, 35, 38, NULL, NULL, NULL, '2025-06-23 23:00:23', '2025-06-23 23:00:23', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (204, 35, 39, NULL, NULL, NULL, '2025-06-23 23:00:23', '2025-06-23 23:00:23', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (205, 35, 40, NULL, NULL, NULL, '2025-06-23 23:27:20', '2025-06-23 23:27:20', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (206, 12, 34, NULL, NULL, NULL, '2025-06-30 05:47:39', '2025-06-30 05:47:39', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (207, 12, 35, NULL, NULL, NULL, '2025-06-30 05:47:39', '2025-06-30 05:47:39', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (208, 13, 34, NULL, NULL, NULL, '2025-06-30 05:48:59', '2025-06-30 05:48:59', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (209, 13, 35, NULL, NULL, NULL, '2025-06-30 05:48:59', '2025-06-30 05:48:59', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (210, 23, 23, NULL, NULL, NULL, '2025-06-30 05:57:51', '2025-06-30 05:57:51', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (211, 23, 24, NULL, NULL, NULL, '2025-06-30 05:57:51', '2025-06-30 05:57:51', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (212, 23, 25, NULL, NULL, NULL, '2025-06-30 05:57:51', '2025-06-30 05:57:51', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (213, 23, 27, NULL, NULL, NULL, '2025-06-30 05:57:51', '2025-06-30 05:57:51', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (214, 23, 28, NULL, NULL, NULL, '2025-06-30 05:57:51', '2025-06-30 05:57:51', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (215, 24, 23, NULL, NULL, NULL, '2025-06-30 05:58:26', '2025-06-30 05:58:26', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (216, 24, 24, NULL, NULL, NULL, '2025-06-30 05:58:26', '2025-06-30 05:58:26', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (217, 24, 25, NULL, NULL, NULL, '2025-06-30 05:58:26', '2025-06-30 05:58:26', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (218, 24, 27, NULL, NULL, NULL, '2025-06-30 05:58:26', '2025-06-30 05:58:26', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (219, 24, 28, NULL, NULL, NULL, '2025-06-30 05:58:26', '2025-06-30 05:58:26', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (220, 25, 23, NULL, NULL, NULL, '2025-06-30 05:59:37', '2025-06-30 05:59:37', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (221, 25, 24, NULL, NULL, NULL, '2025-06-30 05:59:37', '2025-06-30 05:59:37', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (222, 25, 25, NULL, NULL, NULL, '2025-06-30 05:59:37', '2025-06-30 05:59:37', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (223, 25, 27, NULL, NULL, NULL, '2025-06-30 05:59:37', '2025-06-30 05:59:37', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (224, 25, 28, NULL, NULL, NULL, '2025-06-30 05:59:37', '2025-06-30 05:59:37', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (225, 27, 23, NULL, NULL, NULL, '2025-06-30 06:01:12', '2025-06-30 06:01:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (226, 27, 24, NULL, NULL, NULL, '2025-06-30 06:01:12', '2025-06-30 06:01:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (227, 27, 25, NULL, NULL, NULL, '2025-06-30 06:01:12', '2025-06-30 06:01:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (228, 27, 27, NULL, NULL, NULL, '2025-06-30 06:01:12', '2025-06-30 06:01:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (229, 27, 28, NULL, NULL, NULL, '2025-06-30 06:01:12', '2025-06-30 06:01:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (230, 28, 23, NULL, NULL, NULL, '2025-06-30 06:01:35', '2025-06-30 06:01:35', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (231, 28, 24, NULL, NULL, NULL, '2025-06-30 06:01:35', '2025-06-30 06:01:35', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (232, 28, 28, NULL, NULL, NULL, '2025-06-30 06:01:35', '2025-06-30 06:01:35', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (233, 28, 25, NULL, NULL, NULL, '2025-06-30 06:01:35', '2025-06-30 06:01:35', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (234, 28, 27, NULL, NULL, NULL, '2025-06-30 06:01:35', '2025-06-30 06:01:35', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (235, 29, 23, NULL, NULL, NULL, '2025-06-30 06:02:17', '2025-06-30 06:02:17', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (236, 29, 24, NULL, NULL, NULL, '2025-06-30 06:02:17', '2025-06-30 06:02:17', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (237, 29, 25, NULL, NULL, NULL, '2025-06-30 06:02:17', '2025-06-30 06:02:17', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (238, 29, 27, NULL, NULL, NULL, '2025-06-30 06:02:17', '2025-06-30 06:02:17', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (239, 29, 28, NULL, NULL, NULL, '2025-06-30 06:02:17', '2025-06-30 06:02:17', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (240, 31, 23, NULL, NULL, NULL, '2025-06-30 06:02:38', '2025-06-30 06:02:38', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (241, 31, 24, NULL, NULL, NULL, '2025-06-30 06:02:38', '2025-06-30 06:02:38', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (242, 31, 25, NULL, NULL, NULL, '2025-06-30 06:02:38', '2025-06-30 06:02:38', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (243, 31, 28, NULL, NULL, NULL, '2025-06-30 06:02:38', '2025-06-30 06:02:38', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (244, 31, 27, NULL, NULL, NULL, '2025-06-30 06:02:38', '2025-06-30 06:02:38', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (245, 30, 23, NULL, NULL, NULL, '2025-06-30 06:02:58', '2025-06-30 06:02:58', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (246, 30, 25, NULL, NULL, NULL, '2025-06-30 06:02:58', '2025-06-30 06:02:58', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (247, 30, 24, NULL, NULL, NULL, '2025-06-30 06:02:58', '2025-06-30 06:02:58', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (248, 30, 28, NULL, NULL, NULL, '2025-06-30 06:02:58', '2025-06-30 06:02:58', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (249, 30, 27, NULL, NULL, NULL, '2025-06-30 06:02:58', '2025-06-30 06:02:58', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (250, 32, 23, NULL, NULL, NULL, '2025-06-30 06:03:54', '2025-06-30 06:03:54', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (251, 32, 24, NULL, NULL, NULL, '2025-06-30 06:03:54', '2025-06-30 06:03:54', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (252, 32, 25, NULL, NULL, NULL, '2025-06-30 06:03:54', '2025-06-30 06:03:54', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (253, 32, 27, NULL, NULL, NULL, '2025-06-30 06:03:54', '2025-06-30 06:03:54', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (254, 32, 28, NULL, NULL, NULL, '2025-06-30 06:03:54', '2025-06-30 06:03:54', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (255, 36, 23, NULL, NULL, NULL, '2025-06-30 07:18:19', '2025-06-30 07:18:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (256, 36, 24, NULL, NULL, NULL, '2025-06-30 07:18:19', '2025-06-30 07:18:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (257, 36, 25, NULL, NULL, NULL, '2025-06-30 07:18:19', '2025-06-30 07:18:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (258, 36, 27, NULL, NULL, NULL, '2025-06-30 07:18:19', '2025-06-30 07:18:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (259, 36, 28, NULL, NULL, NULL, '2025-06-30 07:18:19', '2025-06-30 07:18:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (265, 30, 32, NULL, NULL, NULL, '2025-07-01 21:40:02', '2025-07-01 21:40:02', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (266, 30, 30, NULL, NULL, NULL, '2025-07-01 21:44:02', '2025-07-01 21:44:02', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (267, 32, 30, NULL, NULL, NULL, '2025-07-01 22:02:48', '2025-07-01 22:02:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (268, 32, 32, NULL, NULL, NULL, '2025-07-01 22:02:48', '2025-07-01 22:02:48', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (269, 12, 41, NULL, NULL, NULL, '2025-07-07 11:30:19', '2025-07-07 11:30:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (276, 40, 23, NULL, NULL, NULL, '2025-08-20 00:39:50', '2025-08-20 00:39:50', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (277, 40, 24, NULL, NULL, NULL, '2025-08-20 00:39:50', '2025-08-20 00:39:50', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (278, 40, 25, NULL, NULL, NULL, '2025-08-20 00:39:50', '2025-08-20 00:39:50', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (279, 40, 27, NULL, NULL, NULL, '2025-08-20 00:39:50', '2025-08-20 00:39:50', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (280, 40, 28, NULL, NULL, NULL, '2025-08-20 00:39:50', '2025-08-20 00:39:50', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (281, 40, 34, NULL, NULL, NULL, '2025-08-27 04:26:12', '2025-08-27 04:26:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (282, 40, 42, NULL, NULL, NULL, '2025-08-27 04:26:12', '2025-08-27 04:26:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (285, 38, 23, NULL, NULL, NULL, '2025-11-18 21:54:08', '2025-11-18 21:54:08', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (286, 38, 24, NULL, NULL, NULL, '2025-11-18 21:54:08', '2025-11-18 21:54:08', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (287, 38, 25, NULL, NULL, NULL, '2025-11-18 21:54:08', '2025-11-18 21:54:08', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (288, 38, 27, NULL, NULL, NULL, '2025-11-18 21:54:08', '2025-11-18 21:54:08', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (289, 38, 28, NULL, NULL, NULL, '2025-11-18 21:54:08', '2025-11-18 21:54:08', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (290, 39, 23, NULL, NULL, NULL, '2025-11-18 22:33:19', '2025-11-18 22:33:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (291, 39, 24, NULL, NULL, NULL, '2025-11-18 22:33:19', '2025-11-18 22:33:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (292, 39, 25, NULL, NULL, NULL, '2025-11-18 22:33:19', '2025-11-18 22:33:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (293, 39, 27, NULL, NULL, NULL, '2025-11-18 22:33:19', '2025-11-18 22:33:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (294, 39, 28, NULL, NULL, NULL, '2025-11-18 22:33:19', '2025-11-18 22:33:19', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (295, 41, 23, NULL, NULL, NULL, '2025-11-18 22:53:12', '2025-11-18 22:53:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (296, 41, 24, NULL, NULL, NULL, '2025-11-18 22:53:12', '2025-11-18 22:53:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (297, 41, 25, NULL, NULL, NULL, '2025-11-18 22:53:12', '2025-11-18 22:53:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (298, 41, 27, NULL, NULL, NULL, '2025-11-18 22:53:12', '2025-11-18 22:53:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (299, 41, 28, NULL, NULL, NULL, '2025-11-18 22:53:12', '2025-11-18 22:53:12', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (300, 41, 30, NULL, NULL, NULL, '2025-11-20 05:48:11', '2025-11-20 05:48:11', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (301, 43, 23, NULL, NULL, NULL, '2025-11-23 13:49:21', '2025-11-23 13:49:21', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (302, 43, 24, NULL, NULL, NULL, '2025-11-23 13:49:21', '2025-11-23 13:49:21', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (303, 43, 25, NULL, NULL, NULL, '2025-11-23 13:49:21', '2025-11-23 13:49:21', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (304, 43, 28, NULL, NULL, NULL, '2025-11-23 13:49:21', '2025-11-23 13:49:21', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (305, 43, 27, NULL, NULL, NULL, '2025-11-23 13:49:21', '2025-11-23 13:49:21', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (306, 42, 23, NULL, NULL, NULL, '2025-11-23 13:49:45', '2025-11-23 13:49:45', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (307, 42, 24, NULL, NULL, NULL, '2025-11-23 13:49:45', '2025-11-23 13:49:45', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (308, 42, 25, NULL, NULL, NULL, '2025-11-23 13:49:45', '2025-11-23 13:49:45', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (309, 42, 27, NULL, NULL, NULL, '2025-11-23 13:49:45', '2025-11-23 13:49:45', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (310, 42, 28, NULL, NULL, NULL, '2025-11-23 13:49:45', '2025-11-23 13:49:45', 'S');
INSERT INTO `sistema_menu_objetos` VALUES (311, 40, 37, NULL, NULL, NULL, '2025-11-28 06:51:43', '2025-11-28 06:51:43', 'S');

-- ----------------------------
-- Table structure for sistema_menu_objetos_roles
-- ----------------------------
DROP TABLE IF EXISTS `sistema_menu_objetos_roles`;
CREATE TABLE `sistema_menu_objetos_roles`  (
  `id_roles` int NULL DEFAULT NULL,
  `id_menu_objetos` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sistema_menu_objetos_roles
-- ----------------------------
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 16, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 14, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 17, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 15, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 208, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (17, 209, '2025-06-30 08:58:42');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 16, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 27, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 29, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 14, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 25, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 186, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 28, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 23, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 24, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (18, 26, '2025-11-18 19:17:54');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 16, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 27, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 188, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 196, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 29, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 14, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 25, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 186, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 194, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 28, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 189, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 195, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 23, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 184, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 192, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 24, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 193, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 15, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 26, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 187, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 201, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 206, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 208, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 207, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 191, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 279, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 278, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 280, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 276, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 277, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 281, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (6, 282, '2025-11-18 19:18:23');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 16, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 188, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 196, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 200, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 298, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 305, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 309, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 300, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 205, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 204, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 203, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 14, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 186, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 194, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 297, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 303, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 308, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 189, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 195, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 199, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 299, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 304, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 310, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 184, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 192, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 197, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 295, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 301, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 306, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 193, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 198, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 296, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 302, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 307, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 15, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 187, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 201, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 202, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 206, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 208, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 207, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 191, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (16, 279, '2025-11-28 01:52:51');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 16, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 27, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 49, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 104, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 115, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 159, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 188, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 196, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 200, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 213, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 218, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 223, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 258, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 298, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 305, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 309, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 29, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 300, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 120, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 108, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 205, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 204, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 203, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 14, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 25, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 58, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 102, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 113, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 157, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 186, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 194, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 212, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 217, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 222, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 257, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 297, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 303, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 308, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 17, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 28, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 50, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 105, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 116, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 160, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 189, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 195, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 199, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 214, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 219, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 224, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 259, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 299, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 304, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 310, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 12, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 23, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 56, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 100, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 111, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 155, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 184, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 192, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 197, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 210, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 215, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 220, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 255, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 295, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 301, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 306, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 269, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 13, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 24, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 57, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 101, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 112, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 156, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 185, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 193, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 198, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 211, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 216, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 221, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 256, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 296, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 302, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 307, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 15, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 26, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 48, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 59, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 103, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 114, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 158, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 187, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 201, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 202, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 206, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 208, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 207, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 209, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 190, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 191, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 253, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 279, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 267, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 268, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 252, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 278, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 254, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 280, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 250, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 276, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 251, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 277, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 228, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 234, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 238, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 244, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 249, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 288, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 293, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 266, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 265, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 227, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 233, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 237, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 242, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 246, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 287, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 292, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 229, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 232, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 239, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 243, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 248, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 289, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 294, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 225, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 230, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 235, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 240, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 245, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 285, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 290, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 226, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 231, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 236, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 241, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 247, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 286, '2025-11-28 01:53:58');
INSERT INTO `sistema_menu_objetos_roles` VALUES (1, 291, '2025-11-28 01:53:58');

-- ----------------------------
-- Table structure for sistema_menu_treeview_temp
-- ----------------------------
DROP TABLE IF EXISTS `sistema_menu_treeview_temp`;
CREATE TABLE `sistema_menu_treeview_temp`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `IdMenu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `IdMenuPadre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `IdObjeto` int NULL DEFAULT NULL,
  `Nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Tipo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Nivel` int NULL DEFAULT NULL,
  `Orden` int NULL DEFAULT NULL,
  `selected` bit(1) NULL DEFAULT NULL,
  `Expanded` bit(1) NULL DEFAULT NULL,
  `Icono` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `id_menu_objetos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sistema_menu_treeview_temp
-- ----------------------------
INSERT INTO `sistema_menu_treeview_temp` VALUES (1, 'm4', NULL, NULL, 'Administración', 'menu', 0, 3, b'0', b'1', 'settings', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (2, 'm5', NULL, NULL, 'Seguridad', 'menu', 0, 1, b'0', b'1', 'security', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (3, 'm32', NULL, NULL, 'Accesos', 'menu', 0, 2, b'1', b'1', 'lock', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (4, 'm40', NULL, NULL, 'Cotizador', 'menu', 0, 4, b'1', b'1', 'widgets', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (8, 'm19', 'm4', NULL, 'Compañia', 'menu', 1, 1, b'0', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (9, 'm26', 'm5', NULL, 'Personal', 'menu', 1, 2, b'0', b'1', 'people', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (10, 'm27', 'm26', NULL, 'Clientes', 'menu', 1, 1, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (11, 'm28', 'm26', NULL, 'Empleados', 'menu', 1, 2, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (12, 'm29', 'm26', NULL, 'Vendedor', 'menu', 1, 3, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (13, 'm30', 'm5', NULL, 'Usuarios', 'menu', 1, 3, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (14, 'm31', 'm26', NULL, 'Proveedores', 'menu', 1, 3, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (15, 'm38', 'm4', NULL, 'Paginas', 'menu', 1, 2, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (16, 'm39', 'm4', NULL, 'Objetos', 'menu', 1, 3, b'1', b'1', 'folder', 'S', NULL);
INSERT INTO `sistema_menu_treeview_temp` VALUES (23, 'o13-23', 'm13', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '12');
INSERT INTO `sistema_menu_treeview_temp` VALUES (24, 'o13-24', 'm13', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '13');
INSERT INTO `sistema_menu_treeview_temp` VALUES (25, 'o13-25', 'm13', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '14');
INSERT INTO `sistema_menu_treeview_temp` VALUES (26, 'o13-26', 'm13', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '15');
INSERT INTO `sistema_menu_treeview_temp` VALUES (27, 'o13-27', 'm13', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '16');
INSERT INTO `sistema_menu_treeview_temp` VALUES (28, 'o13-28', 'm13', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '17');
INSERT INTO `sistema_menu_treeview_temp` VALUES (29, 'o14-23', 'm14', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '23');
INSERT INTO `sistema_menu_treeview_temp` VALUES (30, 'o14-24', 'm14', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '24');
INSERT INTO `sistema_menu_treeview_temp` VALUES (31, 'o14-25', 'm14', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '25');
INSERT INTO `sistema_menu_treeview_temp` VALUES (32, 'o14-26', 'm14', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '26');
INSERT INTO `sistema_menu_treeview_temp` VALUES (33, 'o14-27', 'm14', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '27');
INSERT INTO `sistema_menu_treeview_temp` VALUES (34, 'o14-28', 'm14', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '28');
INSERT INTO `sistema_menu_treeview_temp` VALUES (35, 'o14-29', 'm14', 29, 'AGREGAR MATERIAL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '29');
INSERT INTO `sistema_menu_treeview_temp` VALUES (36, 'o15-23', 'm15', 23, 'EDITAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '34');
INSERT INTO `sistema_menu_treeview_temp` VALUES (37, 'o15-24', 'm15', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '35');
INSERT INTO `sistema_menu_treeview_temp` VALUES (38, 'o15-25', 'm15', 25, 'BUSCAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '36');
INSERT INTO `sistema_menu_treeview_temp` VALUES (39, 'o15-26', 'm15', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '37');
INSERT INTO `sistema_menu_treeview_temp` VALUES (40, 'o15-27', 'm15', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '38');
INSERT INTO `sistema_menu_treeview_temp` VALUES (41, 'o15-28', 'm15', 28, 'CREAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '39');
INSERT INTO `sistema_menu_treeview_temp` VALUES (42, 'o16-23', 'm16', 23, 'EDITAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '45');
INSERT INTO `sistema_menu_treeview_temp` VALUES (43, 'o16-24', 'm16', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '46');
INSERT INTO `sistema_menu_treeview_temp` VALUES (44, 'o16-25', 'm16', 25, 'BUSCAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '47');
INSERT INTO `sistema_menu_treeview_temp` VALUES (45, 'o16-26', 'm16', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '48');
INSERT INTO `sistema_menu_treeview_temp` VALUES (46, 'o16-27', 'm16', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '49');
INSERT INTO `sistema_menu_treeview_temp` VALUES (47, 'o16-28', 'm16', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '50');
INSERT INTO `sistema_menu_treeview_temp` VALUES (48, 'o17-23', 'm17', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '56');
INSERT INTO `sistema_menu_treeview_temp` VALUES (49, 'o17-24', 'm17', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '57');
INSERT INTO `sistema_menu_treeview_temp` VALUES (50, 'o17-25', 'm17', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '58');
INSERT INTO `sistema_menu_treeview_temp` VALUES (51, 'o17-26', 'm17', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '59');
INSERT INTO `sistema_menu_treeview_temp` VALUES (52, 'o17-27', 'm17', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '60');
INSERT INTO `sistema_menu_treeview_temp` VALUES (53, 'o17-28', 'm17', 28, 'CREAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '61');
INSERT INTO `sistema_menu_treeview_temp` VALUES (54, 'o18-23', 'm18', 23, 'EDITAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '67');
INSERT INTO `sistema_menu_treeview_temp` VALUES (55, 'o18-24', 'm18', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '68');
INSERT INTO `sistema_menu_treeview_temp` VALUES (56, 'o18-25', 'm18', 25, 'BUSCAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '69');
INSERT INTO `sistema_menu_treeview_temp` VALUES (57, 'o18-26', 'm18', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '70');
INSERT INTO `sistema_menu_treeview_temp` VALUES (58, 'o18-27', 'm18', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '71');
INSERT INTO `sistema_menu_treeview_temp` VALUES (59, 'o18-28', 'm18', 28, 'CREAR', 'objeto', NULL, NULL, b'0', b'0', 'bullet', 'S', '72');
INSERT INTO `sistema_menu_treeview_temp` VALUES (60, 'o21-23', 'm21', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '100');
INSERT INTO `sistema_menu_treeview_temp` VALUES (61, 'o21-24', 'm21', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '101');
INSERT INTO `sistema_menu_treeview_temp` VALUES (62, 'o21-25', 'm21', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '102');
INSERT INTO `sistema_menu_treeview_temp` VALUES (63, 'o21-26', 'm21', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '103');
INSERT INTO `sistema_menu_treeview_temp` VALUES (64, 'o21-27', 'm21', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '104');
INSERT INTO `sistema_menu_treeview_temp` VALUES (65, 'o21-28', 'm21', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '105');
INSERT INTO `sistema_menu_treeview_temp` VALUES (66, 'o21-31', 'm21', 31, 'AGREGAR ROL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '108');
INSERT INTO `sistema_menu_treeview_temp` VALUES (67, 'o22-23', 'm22', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '111');
INSERT INTO `sistema_menu_treeview_temp` VALUES (68, 'o22-24', 'm22', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '112');
INSERT INTO `sistema_menu_treeview_temp` VALUES (69, 'o22-25', 'm22', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '113');
INSERT INTO `sistema_menu_treeview_temp` VALUES (70, 'o22-26', 'm22', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '114');
INSERT INTO `sistema_menu_treeview_temp` VALUES (71, 'o22-27', 'm22', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '115');
INSERT INTO `sistema_menu_treeview_temp` VALUES (72, 'o22-28', 'm22', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '116');
INSERT INTO `sistema_menu_treeview_temp` VALUES (73, 'o22-32', 'm22', 32, 'AGREGAR PERMISO', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '120');
INSERT INTO `sistema_menu_treeview_temp` VALUES (74, 'o34-23', 'm34', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '155');
INSERT INTO `sistema_menu_treeview_temp` VALUES (75, 'o34-24', 'm34', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '156');
INSERT INTO `sistema_menu_treeview_temp` VALUES (76, 'o34-25', 'm34', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '157');
INSERT INTO `sistema_menu_treeview_temp` VALUES (77, 'o34-26', 'm34', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '158');
INSERT INTO `sistema_menu_treeview_temp` VALUES (78, 'o34-27', 'm34', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '159');
INSERT INTO `sistema_menu_treeview_temp` VALUES (79, 'o34-28', 'm34', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '160');
INSERT INTO `sistema_menu_treeview_temp` VALUES (80, 'o12-23', 'm12', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '184');
INSERT INTO `sistema_menu_treeview_temp` VALUES (81, 'o12-24', 'm12', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '185');
INSERT INTO `sistema_menu_treeview_temp` VALUES (82, 'o12-25', 'm12', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '186');
INSERT INTO `sistema_menu_treeview_temp` VALUES (83, 'o12-26', 'm12', 26, 'EXPORTAR EXCEL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '187');
INSERT INTO `sistema_menu_treeview_temp` VALUES (84, 'o12-27', 'm12', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '188');
INSERT INTO `sistema_menu_treeview_temp` VALUES (85, 'o12-28', 'm12', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '189');
INSERT INTO `sistema_menu_treeview_temp` VALUES (86, 'o12-33', 'm12', 33, 'LINK CARGUIO', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '190');
INSERT INTO `sistema_menu_treeview_temp` VALUES (87, 'o12-36', 'm12', 36, 'REFRESCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '191');
INSERT INTO `sistema_menu_treeview_temp` VALUES (88, 'o33-23', 'm33', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '192');
INSERT INTO `sistema_menu_treeview_temp` VALUES (89, 'o33-24', 'm33', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '193');
INSERT INTO `sistema_menu_treeview_temp` VALUES (90, 'o33-25', 'm33', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '194');
INSERT INTO `sistema_menu_treeview_temp` VALUES (91, 'o33-28', 'm33', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '195');
INSERT INTO `sistema_menu_treeview_temp` VALUES (92, 'o33-27', 'm33', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '196');
INSERT INTO `sistema_menu_treeview_temp` VALUES (93, 'o35-23', 'm35', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '197');
INSERT INTO `sistema_menu_treeview_temp` VALUES (94, 'o35-24', 'm35', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '198');
INSERT INTO `sistema_menu_treeview_temp` VALUES (95, 'o35-28', 'm35', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '199');
INSERT INTO `sistema_menu_treeview_temp` VALUES (96, 'o35-27', 'm35', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '200');
INSERT INTO `sistema_menu_treeview_temp` VALUES (97, 'o33-37', 'm33', 37, 'EXPORTAR PDF', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '201');
INSERT INTO `sistema_menu_treeview_temp` VALUES (98, 'o35-37', 'm35', 37, 'EXPORTAR PDF', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '202');
INSERT INTO `sistema_menu_treeview_temp` VALUES (99, 'o35-38', 'm35', 38, 'BOTONES DE INGRESO', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '203');
INSERT INTO `sistema_menu_treeview_temp` VALUES (100, 'o35-39', 'm35', 39, 'BOTONES DE EGRESO', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '204');
INSERT INTO `sistema_menu_treeview_temp` VALUES (101, 'o35-40', 'm35', 40, 'BOTON DE INGRESO A CAJA CHICA', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '205');
INSERT INTO `sistema_menu_treeview_temp` VALUES (102, 'o12-34', 'm12', 34, 'FILTRAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '206');
INSERT INTO `sistema_menu_treeview_temp` VALUES (103, 'o12-35', 'm12', 35, 'LIMPIAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '207');
INSERT INTO `sistema_menu_treeview_temp` VALUES (104, 'o13-34', 'm13', 34, 'FILTRAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '208');
INSERT INTO `sistema_menu_treeview_temp` VALUES (105, 'o13-35', 'm13', 35, 'LIMPIAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '209');
INSERT INTO `sistema_menu_treeview_temp` VALUES (106, 'o23-23', 'm23', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '210');
INSERT INTO `sistema_menu_treeview_temp` VALUES (107, 'o23-24', 'm23', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '211');
INSERT INTO `sistema_menu_treeview_temp` VALUES (108, 'o23-25', 'm23', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '212');
INSERT INTO `sistema_menu_treeview_temp` VALUES (109, 'o23-27', 'm23', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '213');
INSERT INTO `sistema_menu_treeview_temp` VALUES (110, 'o23-28', 'm23', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '214');
INSERT INTO `sistema_menu_treeview_temp` VALUES (111, 'o24-23', 'm24', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '215');
INSERT INTO `sistema_menu_treeview_temp` VALUES (112, 'o24-24', 'm24', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '216');
INSERT INTO `sistema_menu_treeview_temp` VALUES (113, 'o24-25', 'm24', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '217');
INSERT INTO `sistema_menu_treeview_temp` VALUES (114, 'o24-27', 'm24', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '218');
INSERT INTO `sistema_menu_treeview_temp` VALUES (115, 'o24-28', 'm24', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '219');
INSERT INTO `sistema_menu_treeview_temp` VALUES (116, 'o25-23', 'm25', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '220');
INSERT INTO `sistema_menu_treeview_temp` VALUES (117, 'o25-24', 'm25', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '221');
INSERT INTO `sistema_menu_treeview_temp` VALUES (118, 'o25-25', 'm25', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '222');
INSERT INTO `sistema_menu_treeview_temp` VALUES (119, 'o25-27', 'm25', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '223');
INSERT INTO `sistema_menu_treeview_temp` VALUES (120, 'o25-28', 'm25', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '224');
INSERT INTO `sistema_menu_treeview_temp` VALUES (121, 'o27-23', 'm27', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '225');
INSERT INTO `sistema_menu_treeview_temp` VALUES (122, 'o27-24', 'm27', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '226');
INSERT INTO `sistema_menu_treeview_temp` VALUES (123, 'o27-25', 'm27', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '227');
INSERT INTO `sistema_menu_treeview_temp` VALUES (124, 'o27-27', 'm27', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '228');
INSERT INTO `sistema_menu_treeview_temp` VALUES (125, 'o27-28', 'm27', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '229');
INSERT INTO `sistema_menu_treeview_temp` VALUES (126, 'o28-23', 'm28', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '230');
INSERT INTO `sistema_menu_treeview_temp` VALUES (127, 'o28-24', 'm28', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '231');
INSERT INTO `sistema_menu_treeview_temp` VALUES (128, 'o28-28', 'm28', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '232');
INSERT INTO `sistema_menu_treeview_temp` VALUES (129, 'o28-25', 'm28', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '233');
INSERT INTO `sistema_menu_treeview_temp` VALUES (130, 'o28-27', 'm28', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '234');
INSERT INTO `sistema_menu_treeview_temp` VALUES (131, 'o29-23', 'm29', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '235');
INSERT INTO `sistema_menu_treeview_temp` VALUES (132, 'o29-24', 'm29', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '236');
INSERT INTO `sistema_menu_treeview_temp` VALUES (133, 'o29-25', 'm29', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '237');
INSERT INTO `sistema_menu_treeview_temp` VALUES (134, 'o29-27', 'm29', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '238');
INSERT INTO `sistema_menu_treeview_temp` VALUES (135, 'o29-28', 'm29', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '239');
INSERT INTO `sistema_menu_treeview_temp` VALUES (136, 'o31-23', 'm31', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '240');
INSERT INTO `sistema_menu_treeview_temp` VALUES (137, 'o31-24', 'm31', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '241');
INSERT INTO `sistema_menu_treeview_temp` VALUES (138, 'o31-25', 'm31', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '242');
INSERT INTO `sistema_menu_treeview_temp` VALUES (139, 'o31-28', 'm31', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '243');
INSERT INTO `sistema_menu_treeview_temp` VALUES (140, 'o31-27', 'm31', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '244');
INSERT INTO `sistema_menu_treeview_temp` VALUES (141, 'o30-23', 'm30', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '245');
INSERT INTO `sistema_menu_treeview_temp` VALUES (142, 'o30-25', 'm30', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '246');
INSERT INTO `sistema_menu_treeview_temp` VALUES (143, 'o30-24', 'm30', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '247');
INSERT INTO `sistema_menu_treeview_temp` VALUES (144, 'o30-28', 'm30', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '248');
INSERT INTO `sistema_menu_treeview_temp` VALUES (145, 'o30-27', 'm30', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '249');
INSERT INTO `sistema_menu_treeview_temp` VALUES (146, 'o32-23', 'm32', 23, 'EDITAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '250');
INSERT INTO `sistema_menu_treeview_temp` VALUES (147, 'o32-24', 'm32', 24, 'ELIMINAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '251');
INSERT INTO `sistema_menu_treeview_temp` VALUES (148, 'o32-25', 'm32', 25, 'BUSCAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '252');
INSERT INTO `sistema_menu_treeview_temp` VALUES (149, 'o32-27', 'm32', 27, 'ACTUALIZAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '253');
INSERT INTO `sistema_menu_treeview_temp` VALUES (150, 'o32-28', 'm32', 28, 'CREAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '254');
INSERT INTO `sistema_menu_treeview_temp` VALUES (151, 'o36-23', 'm36', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '255');
INSERT INTO `sistema_menu_treeview_temp` VALUES (152, 'o36-24', 'm36', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '256');
INSERT INTO `sistema_menu_treeview_temp` VALUES (153, 'o36-25', 'm36', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '257');
INSERT INTO `sistema_menu_treeview_temp` VALUES (154, 'o36-27', 'm36', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '258');
INSERT INTO `sistema_menu_treeview_temp` VALUES (155, 'o36-28', 'm36', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '259');
INSERT INTO `sistema_menu_treeview_temp` VALUES (156, 'o30-32', 'm30', 32, 'AGREGAR PERMISO', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '265');
INSERT INTO `sistema_menu_treeview_temp` VALUES (157, 'o30-30', 'm30', 30, 'AGREGAR PERFIL', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '266');
INSERT INTO `sistema_menu_treeview_temp` VALUES (158, 'o32-30', 'm32', 30, 'AGREGAR PERFIL', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '267');
INSERT INTO `sistema_menu_treeview_temp` VALUES (159, 'o32-32', 'm32', 32, 'AGREGAR PERMISO', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '268');
INSERT INTO `sistema_menu_treeview_temp` VALUES (160, 'o12-41', 'm12', 41, 'EDITAR VOLUMEN DE CARGA', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '269');
INSERT INTO `sistema_menu_treeview_temp` VALUES (161, 'o40-23', 'm40', 23, 'EDITAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '276');
INSERT INTO `sistema_menu_treeview_temp` VALUES (162, 'o40-24', 'm40', 24, 'ELIMINAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '277');
INSERT INTO `sistema_menu_treeview_temp` VALUES (163, 'o40-25', 'm40', 25, 'BUSCAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '278');
INSERT INTO `sistema_menu_treeview_temp` VALUES (164, 'o40-27', 'm40', 27, 'ACTUALIZAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '279');
INSERT INTO `sistema_menu_treeview_temp` VALUES (165, 'o40-28', 'm40', 28, 'CREAR', 'objeto', 1, NULL, b'1', b'0', 'bullet', 'S', '280');
INSERT INTO `sistema_menu_treeview_temp` VALUES (166, 'o40-34', 'm40', 34, 'FILTRAR', 'objeto', 1, NULL, b'0', b'0', 'bullet', 'S', '281');
INSERT INTO `sistema_menu_treeview_temp` VALUES (167, 'o40-42', 'm40', 42, 'VER', 'objeto', 1, NULL, b'0', b'0', 'bullet', 'S', '282');
INSERT INTO `sistema_menu_treeview_temp` VALUES (168, 'o38-23', 'm38', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '285');
INSERT INTO `sistema_menu_treeview_temp` VALUES (169, 'o38-24', 'm38', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '286');
INSERT INTO `sistema_menu_treeview_temp` VALUES (170, 'o38-25', 'm38', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '287');
INSERT INTO `sistema_menu_treeview_temp` VALUES (171, 'o38-27', 'm38', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '288');
INSERT INTO `sistema_menu_treeview_temp` VALUES (172, 'o38-28', 'm38', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '289');
INSERT INTO `sistema_menu_treeview_temp` VALUES (173, 'o39-23', 'm39', 23, 'EDITAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '290');
INSERT INTO `sistema_menu_treeview_temp` VALUES (174, 'o39-24', 'm39', 24, 'ELIMINAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '291');
INSERT INTO `sistema_menu_treeview_temp` VALUES (175, 'o39-25', 'm39', 25, 'BUSCAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '292');
INSERT INTO `sistema_menu_treeview_temp` VALUES (176, 'o39-27', 'm39', 27, 'ACTUALIZAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '293');
INSERT INTO `sistema_menu_treeview_temp` VALUES (177, 'o39-28', 'm39', 28, 'CREAR', 'objeto', 2, NULL, b'1', b'0', 'bullet', 'S', '294');
INSERT INTO `sistema_menu_treeview_temp` VALUES (178, 'o41-23', 'm41', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '295');
INSERT INTO `sistema_menu_treeview_temp` VALUES (179, 'o41-24', 'm41', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '296');
INSERT INTO `sistema_menu_treeview_temp` VALUES (180, 'o41-25', 'm41', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '297');
INSERT INTO `sistema_menu_treeview_temp` VALUES (181, 'o41-27', 'm41', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '298');
INSERT INTO `sistema_menu_treeview_temp` VALUES (182, 'o41-28', 'm41', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '299');
INSERT INTO `sistema_menu_treeview_temp` VALUES (183, 'o41-30', 'm41', 30, 'AGREGAR PERFIL', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '300');
INSERT INTO `sistema_menu_treeview_temp` VALUES (184, 'o43-23', 'm43', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '301');
INSERT INTO `sistema_menu_treeview_temp` VALUES (185, 'o43-24', 'm43', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '302');
INSERT INTO `sistema_menu_treeview_temp` VALUES (186, 'o43-25', 'm43', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '303');
INSERT INTO `sistema_menu_treeview_temp` VALUES (187, 'o43-28', 'm43', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '304');
INSERT INTO `sistema_menu_treeview_temp` VALUES (188, 'o43-27', 'm43', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '305');
INSERT INTO `sistema_menu_treeview_temp` VALUES (189, 'o42-23', 'm42', 23, 'EDITAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '306');
INSERT INTO `sistema_menu_treeview_temp` VALUES (190, 'o42-24', 'm42', 24, 'ELIMINAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '307');
INSERT INTO `sistema_menu_treeview_temp` VALUES (191, 'o42-25', 'm42', 25, 'BUSCAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '308');
INSERT INTO `sistema_menu_treeview_temp` VALUES (192, 'o42-27', 'm42', 27, 'ACTUALIZAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '309');
INSERT INTO `sistema_menu_treeview_temp` VALUES (193, 'o42-28', 'm42', 28, 'CREAR', 'objeto', NULL, NULL, b'1', b'0', 'bullet', 'S', '310');
INSERT INTO `sistema_menu_treeview_temp` VALUES (194, 'o40-37', 'm40', 37, 'EXPORTAR PDF', 'objeto', 1, NULL, b'0', b'0', 'bullet', 'S', '311');

-- ----------------------------
-- Table structure for sistema_objetos
-- ----------------------------
DROP TABLE IF EXISTS `sistema_objetos`;
CREATE TABLE `sistema_objetos`  (
  `id_objetos` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  PRIMARY KEY (`id_objetos`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sistema_objetos
-- ----------------------------
INSERT INTO `sistema_objetos` VALUES (23, 'EDITAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (24, 'ELIMINAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (25, 'BUSCAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (26, 'EXPORTAR EXCEL', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (27, 'ACTUALIZAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (28, 'CREAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (29, 'AGREGAR MATERIAL', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (30, 'AGREGAR PERFIL', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (31, 'AGREGAR ROL', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (32, 'AGREGAR PERMISO', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (33, 'LINK CARGUIO', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (34, 'FILTRAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (35, 'LIMPIAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (36, 'REFRESCAR', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (37, 'EXPORTAR PDF', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (38, 'BOTONES DE INGRESO', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (39, 'BOTONES DE EGRESO', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (40, 'BOTON DE INGRESO A CAJA CHICA', '2025-06-30 21:04:53', '2025-06-30 21:04:53', 'S');
INSERT INTO `sistema_objetos` VALUES (41, 'EDITAR VOLUMEN DE CARGA', '2025-07-07 11:29:58', '2025-07-07 11:29:58', 'S');
INSERT INTO `sistema_objetos` VALUES (42, 'VER', '2025-08-27 00:31:03', '2025-08-27 00:31:03', 'S');

-- ----------------------------
-- Table structure for ubigeo_paises
-- ----------------------------
DROP TABLE IF EXISTS `ubigeo_paises`;
CREATE TABLE `ubigeo_paises`  (
  `id` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ubigeo_paises
-- ----------------------------
INSERT INTO `ubigeo_paises` VALUES ('PE', 'Perú');

-- ----------------------------
-- Table structure for ubigeo_peru_departments
-- ----------------------------
DROP TABLE IF EXISTS `ubigeo_peru_departments`;
CREATE TABLE `ubigeo_peru_departments`  (
  `id` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `id_pais` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ubigeo_peru_departments
-- ----------------------------
INSERT INTO `ubigeo_peru_departments` VALUES ('01', 'Amazonas', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('02', 'Áncash', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('03', 'Apurímac', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('04', 'Arequipa', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('05', 'Ayacucho', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('06', 'Cajamarca', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('07', 'Callao', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('08', 'Cusco', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('09', 'Huancavelica', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('10', 'Huánuco', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('11', 'Ica', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('12', 'Junín', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('13', 'La Libertad', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('14', 'Lambayeque', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('15', 'Lima', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('16', 'Loreto', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('17', 'Madre de Dios', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('18', 'Moquegua', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('19', 'Pasco', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('20', 'Piura', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('21', 'Puno', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('22', 'San Martín', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('23', 'Tacna', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('24', 'Tumbes', 'PE');
INSERT INTO `ubigeo_peru_departments` VALUES ('25', 'Ucayali', 'PE');

-- ----------------------------
-- Table structure for ubigeo_peru_districts
-- ----------------------------
DROP TABLE IF EXISTS `ubigeo_peru_districts`;
CREATE TABLE `ubigeo_peru_districts`  (
  `id` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `id_province` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `id_department` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `id_pais` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ubigeo_peru_districts
-- ----------------------------
INSERT INTO `ubigeo_peru_districts` VALUES ('010101', 'Chachapoyas', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010102', 'Asunción', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010103', 'Balsas', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010104', 'Cheto', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010105', 'Chiliquin', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010106', 'Chuquibamba', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010107', 'Granada', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010108', 'Huancas', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010109', 'La Jalca', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010110', 'Leimebamba', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010111', 'Levanto', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010112', 'Magdalena', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010113', 'Mariscal Castilla', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010114', 'Molinopampa', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010115', 'Montevideo', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010116', 'Olleros', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010117', 'Quinjalca', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010118', 'San Francisco de Daguas', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010119', 'San Isidro de Maino', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010120', 'Soloco', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010121', 'Sonche', '0101', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010201', 'Bagua', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010202', 'Aramango', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010203', 'Copallin', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010204', 'El Parco', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010205', 'Imaza', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010206', 'La Peca', '0102', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010301', 'Jumbilla', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010302', 'Chisquilla', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010303', 'Churuja', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010304', 'Corosha', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010305', 'Cuispes', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010306', 'Florida', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010307', 'Jazan', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010308', 'Recta', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010309', 'San Carlos', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010310', 'Shipasbamba', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010311', 'Valera', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010312', 'Yambrasbamba', '0103', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010401', 'Nieva', '0104', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010402', 'El Cenepa', '0104', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010403', 'Río Santiago', '0104', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010501', 'Lamud', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010502', 'Camporredondo', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010503', 'Cocabamba', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010504', 'Colcamar', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010505', 'Conila', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010506', 'Inguilpata', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010507', 'Longuita', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010508', 'Lonya Chico', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010509', 'Luya', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010510', 'Luya Viejo', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010511', 'María', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010512', 'Ocalli', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010513', 'Ocumal', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010514', 'Pisuquia', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010515', 'Providencia', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010516', 'San Cristóbal', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010517', 'San Francisco de Yeso', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010518', 'San Jerónimo', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010519', 'San Juan de Lopecancha', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010520', 'Santa Catalina', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010521', 'Santo Tomas', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010522', 'Tingo', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010523', 'Trita', '0105', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010601', 'San Nicolás', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010602', 'Chirimoto', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010603', 'Cochamal', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010604', 'Huambo', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010605', 'Limabamba', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010606', 'Longar', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010607', 'Mariscal Benavides', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010608', 'Milpuc', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010609', 'Omia', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010610', 'Santa Rosa', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010611', 'Totora', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010612', 'Vista Alegre', '0106', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010701', 'Bagua Grande', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010702', 'Cajaruro', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010703', 'Cumba', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010704', 'El Milagro', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010705', 'Jamalca', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010706', 'Lonya Grande', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('010707', 'Yamon', '0107', '01', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020101', 'Huaraz', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020102', 'Cochabamba', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020103', 'Colcabamba', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020104', 'Huanchay', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020105', 'Independencia', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020106', 'Jangas', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020107', 'La Libertad', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020108', 'Olleros', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020109', 'Pampas Grande', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020110', 'Pariacoto', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020111', 'Pira', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020112', 'Tarica', '0201', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020201', 'Aija', '0202', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020202', 'Coris', '0202', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020203', 'Huacllan', '0202', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020204', 'La Merced', '0202', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020205', 'Succha', '0202', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020301', 'Llamellin', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020302', 'Aczo', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020303', 'Chaccho', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020304', 'Chingas', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020305', 'Mirgas', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020306', 'San Juan de Rontoy', '0203', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020401', 'Chacas', '0204', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020402', 'Acochaca', '0204', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020501', 'Chiquian', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020502', 'Abelardo Pardo Lezameta', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020503', 'Antonio Raymondi', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020504', 'Aquia', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020505', 'Cajacay', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020506', 'Canis', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020507', 'Colquioc', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020508', 'Huallanca', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020509', 'Huasta', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020510', 'Huayllacayan', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020511', 'La Primavera', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020512', 'Mangas', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020513', 'Pacllon', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020514', 'San Miguel de Corpanqui', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020515', 'Ticllos', '0205', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020601', 'Carhuaz', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020602', 'Acopampa', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020603', 'Amashca', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020604', 'Anta', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020605', 'Ataquero', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020606', 'Marcara', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020607', 'Pariahuanca', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020608', 'San Miguel de Aco', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020609', 'Shilla', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020610', 'Tinco', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020611', 'Yungar', '0206', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020701', 'San Luis', '0207', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020702', 'San Nicolás', '0207', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020703', 'Yauya', '0207', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020801', 'Casma', '0208', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020802', 'Buena Vista Alta', '0208', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020803', 'Comandante Noel', '0208', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020804', 'Yautan', '0208', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020901', 'Corongo', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020902', 'Aco', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020903', 'Bambas', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020904', 'Cusca', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020905', 'La Pampa', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020906', 'Yanac', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('020907', 'Yupan', '0209', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021001', 'Huari', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021002', 'Anra', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021003', 'Cajay', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021004', 'Chavin de Huantar', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021005', 'Huacachi', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021006', 'Huacchis', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021007', 'Huachis', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021008', 'Huantar', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021009', 'Masin', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021010', 'Paucas', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021011', 'Ponto', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021012', 'Rahuapampa', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021013', 'Rapayan', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021014', 'San Marcos', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021015', 'San Pedro de Chana', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021016', 'Uco', '0210', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021101', 'Huarmey', '0211', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021102', 'Cochapeti', '0211', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021103', 'Culebras', '0211', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021104', 'Huayan', '0211', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021105', 'Malvas', '0211', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021201', 'Caraz', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021202', 'Huallanca', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021203', 'Huata', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021204', 'Huaylas', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021205', 'Mato', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021206', 'Pamparomas', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021207', 'Pueblo Libre', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021208', 'Santa Cruz', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021209', 'Santo Toribio', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021210', 'Yuracmarca', '0212', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021301', 'Piscobamba', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021302', 'Casca', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021303', 'Eleazar Guzmán Barron', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021304', 'Fidel Olivas Escudero', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021305', 'Llama', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021306', 'Llumpa', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021307', 'Lucma', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021308', 'Musga', '0213', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021401', 'Ocros', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021402', 'Acas', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021403', 'Cajamarquilla', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021404', 'Carhuapampa', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021405', 'Cochas', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021406', 'Congas', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021407', 'Llipa', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021408', 'San Cristóbal de Rajan', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021409', 'San Pedro', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021410', 'Santiago de Chilcas', '0214', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021501', 'Cabana', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021502', 'Bolognesi', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021503', 'Conchucos', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021504', 'Huacaschuque', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021505', 'Huandoval', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021506', 'Lacabamba', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021507', 'Llapo', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021508', 'Pallasca', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021509', 'Pampas', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021510', 'Santa Rosa', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021511', 'Tauca', '0215', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021601', 'Pomabamba', '0216', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021602', 'Huayllan', '0216', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021603', 'Parobamba', '0216', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021604', 'Quinuabamba', '0216', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021701', 'Recuay', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021702', 'Catac', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021703', 'Cotaparaco', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021704', 'Huayllapampa', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021705', 'Llacllin', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021706', 'Marca', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021707', 'Pampas Chico', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021708', 'Pararin', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021709', 'Tapacocha', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021710', 'Ticapampa', '0217', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021801', 'Chimbote', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021802', 'Cáceres del Perú', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021803', 'Coishco', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021804', 'Macate', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021805', 'Moro', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021806', 'Nepeña', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021807', 'Samanco', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021808', 'Santa', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021809', 'Nuevo Chimbote', '0218', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021901', 'Sihuas', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021902', 'Acobamba', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021903', 'Alfonso Ugarte', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021904', 'Cashapampa', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021905', 'Chingalpo', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021906', 'Huayllabamba', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021907', 'Quiches', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021908', 'Ragash', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021909', 'San Juan', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('021910', 'Sicsibamba', '0219', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022001', 'Yungay', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022002', 'Cascapara', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022003', 'Mancos', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022004', 'Matacoto', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022005', 'Quillo', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022006', 'Ranrahirca', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022007', 'Shupluy', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('022008', 'Yanama', '0220', '02', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030101', 'Abancay', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030102', 'Chacoche', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030103', 'Circa', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030104', 'Curahuasi', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030105', 'Huanipaca', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030106', 'Lambrama', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030107', 'Pichirhua', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030108', 'San Pedro de Cachora', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030109', 'Tamburco', '0301', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030201', 'Andahuaylas', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030202', 'Andarapa', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030203', 'Chiara', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030204', 'Huancarama', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030205', 'Huancaray', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030206', 'Huayana', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030207', 'Kishuara', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030208', 'Pacobamba', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030209', 'Pacucha', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030210', 'Pampachiri', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030211', 'Pomacocha', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030212', 'San Antonio de Cachi', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030213', 'San Jerónimo', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030214', 'San Miguel de Chaccrampa', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030215', 'Santa María de Chicmo', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030216', 'Talavera', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030217', 'Tumay Huaraca', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030218', 'Turpo', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030219', 'Kaquiabamba', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030220', 'José María Arguedas', '0302', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030301', 'Antabamba', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030302', 'El Oro', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030303', 'Huaquirca', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030304', 'Juan Espinoza Medrano', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030305', 'Oropesa', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030306', 'Pachaconas', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030307', 'Sabaino', '0303', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030401', 'Chalhuanca', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030402', 'Capaya', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030403', 'Caraybamba', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030404', 'Chapimarca', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030405', 'Colcabamba', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030406', 'Cotaruse', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030407', 'Ihuayllo', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030408', 'Justo Apu Sahuaraura', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030409', 'Lucre', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030410', 'Pocohuanca', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030411', 'San Juan de Chacña', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030412', 'Sañayca', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030413', 'Soraya', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030414', 'Tapairihua', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030415', 'Tintay', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030416', 'Toraya', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030417', 'Yanaca', '0304', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030501', 'Tambobamba', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030502', 'Cotabambas', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030503', 'Coyllurqui', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030504', 'Haquira', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030505', 'Mara', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030506', 'Challhuahuacho', '0305', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030601', 'Chincheros', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030602', 'Anco_Huallo', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030603', 'Cocharcas', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030604', 'Huaccana', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030605', 'Ocobamba', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030606', 'Ongoy', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030607', 'Uranmarca', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030608', 'Ranracancha', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030609', 'Rocchacc', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030610', 'El Porvenir', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030611', 'Los Chankas', '0306', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030701', 'Chuquibambilla', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030702', 'Curpahuasi', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030703', 'Gamarra', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030704', 'Huayllati', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030705', 'Mamara', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030706', 'Micaela Bastidas', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030707', 'Pataypampa', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030708', 'Progreso', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030709', 'San Antonio', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030710', 'Santa Rosa', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030711', 'Turpay', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030712', 'Vilcabamba', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030713', 'Virundo', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('030714', 'Curasco', '0307', '03', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040101', 'Arequipa', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040102', 'Alto Selva Alegre', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040103', 'Cayma', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040104', 'Cerro Colorado', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040105', 'Characato', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040106', 'Chiguata', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040107', 'Jacobo Hunter', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040108', 'La Joya', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040109', 'Mariano Melgar', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040110', 'Miraflores', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040111', 'Mollebaya', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040112', 'Paucarpata', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040113', 'Pocsi', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040114', 'Polobaya', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040115', 'Quequeña', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040116', 'Sabandia', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040117', 'Sachaca', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040118', 'San Juan de Siguas', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040119', 'San Juan de Tarucani', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040120', 'Santa Isabel de Siguas', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040121', 'Santa Rita de Siguas', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040122', 'Socabaya', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040123', 'Tiabaya', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040124', 'Uchumayo', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040125', 'Vitor', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040126', 'Yanahuara', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040127', 'Yarabamba', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040128', 'Yura', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040129', 'José Luis Bustamante Y Rivero', '0401', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040201', 'Camaná', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040202', 'José María Quimper', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040203', 'Mariano Nicolás Valcárcel', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040204', 'Mariscal Cáceres', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040205', 'Nicolás de Pierola', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040206', 'Ocoña', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040207', 'Quilca', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040208', 'Samuel Pastor', '0402', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040301', 'Caravelí', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040302', 'Acarí', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040303', 'Atico', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040304', 'Atiquipa', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040305', 'Bella Unión', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040306', 'Cahuacho', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040307', 'Chala', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040308', 'Chaparra', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040309', 'Huanuhuanu', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040310', 'Jaqui', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040311', 'Lomas', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040312', 'Quicacha', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040313', 'Yauca', '0403', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040401', 'Aplao', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040402', 'Andagua', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040403', 'Ayo', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040404', 'Chachas', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040405', 'Chilcaymarca', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040406', 'Choco', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040407', 'Huancarqui', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040408', 'Machaguay', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040409', 'Orcopampa', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040410', 'Pampacolca', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040411', 'Tipan', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040412', 'Uñon', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040413', 'Uraca', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040414', 'Viraco', '0404', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040501', 'Chivay', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040502', 'Achoma', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040503', 'Cabanaconde', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040504', 'Callalli', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040505', 'Caylloma', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040506', 'Coporaque', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040507', 'Huambo', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040508', 'Huanca', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040509', 'Ichupampa', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040510', 'Lari', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040511', 'Lluta', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040512', 'Maca', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040513', 'Madrigal', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040514', 'San Antonio de Chuca', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040515', 'Sibayo', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040516', 'Tapay', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040517', 'Tisco', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040518', 'Tuti', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040519', 'Yanque', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040520', 'Majes', '0405', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040601', 'Chuquibamba', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040602', 'Andaray', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040603', 'Cayarani', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040604', 'Chichas', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040605', 'Iray', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040606', 'Río Grande', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040607', 'Salamanca', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040608', 'Yanaquihua', '0406', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040701', 'Mollendo', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040702', 'Cocachacra', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040703', 'Dean Valdivia', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040704', 'Islay', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040705', 'Mejia', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040706', 'Punta de Bombón', '0407', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040801', 'Cotahuasi', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040802', 'Alca', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040803', 'Charcana', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040804', 'Huaynacotas', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040805', 'Pampamarca', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040806', 'Puyca', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040807', 'Quechualla', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040808', 'Sayla', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040809', 'Tauria', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040810', 'Tomepampa', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('040811', 'Toro', '0408', '04', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050101', 'Ayacucho', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050102', 'Acocro', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050103', 'Acos Vinchos', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050104', 'Carmen Alto', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050105', 'Chiara', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050106', 'Ocros', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050107', 'Pacaycasa', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050108', 'Quinua', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050109', 'San José de Ticllas', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050110', 'San Juan Bautista', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050111', 'Santiago de Pischa', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050112', 'Socos', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050113', 'Tambillo', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050114', 'Vinchos', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050115', 'Jesús Nazareno', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050116', 'Andrés Avelino Cáceres Dorregaray', '0501', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050201', 'Cangallo', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050202', 'Chuschi', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050203', 'Los Morochucos', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050204', 'María Parado de Bellido', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050205', 'Paras', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050206', 'Totos', '0502', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050301', 'Sancos', '0503', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050302', 'Carapo', '0503', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050303', 'Sacsamarca', '0503', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050304', 'Santiago de Lucanamarca', '0503', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050401', 'Huanta', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050402', 'Ayahuanco', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050403', 'Huamanguilla', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050404', 'Iguain', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050405', 'Luricocha', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050406', 'Santillana', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050407', 'Sivia', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050408', 'Llochegua', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050409', 'Canayre', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050410', 'Uchuraccay', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050411', 'Pucacolpa', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050412', 'Chaca', '0504', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050501', 'San Miguel', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050502', 'Anco', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050503', 'Ayna', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050504', 'Chilcas', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050505', 'Chungui', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050506', 'Luis Carranza', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050507', 'Santa Rosa', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050508', 'Tambo', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050509', 'Samugari', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050510', 'Anchihuay', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050511', 'Oronccoy', '0505', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050601', 'Puquio', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050602', 'Aucara', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050603', 'Cabana', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050604', 'Carmen Salcedo', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050605', 'Chaviña', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050606', 'Chipao', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050607', 'Huac-Huas', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050608', 'Laramate', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050609', 'Leoncio Prado', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050610', 'Llauta', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050611', 'Lucanas', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050612', 'Ocaña', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050613', 'Otoca', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050614', 'Saisa', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050615', 'San Cristóbal', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050616', 'San Juan', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050617', 'San Pedro', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050618', 'San Pedro de Palco', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050619', 'Sancos', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050620', 'Santa Ana de Huaycahuacho', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050621', 'Santa Lucia', '0506', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050701', 'Coracora', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050702', 'Chumpi', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050703', 'Coronel Castañeda', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050704', 'Pacapausa', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050705', 'Pullo', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050706', 'Puyusca', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050707', 'San Francisco de Ravacayco', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050708', 'Upahuacho', '0507', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050801', 'Pausa', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050802', 'Colta', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050803', 'Corculla', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050804', 'Lampa', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050805', 'Marcabamba', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050806', 'Oyolo', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050807', 'Pararca', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050808', 'San Javier de Alpabamba', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050809', 'San José de Ushua', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050810', 'Sara Sara', '0508', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050901', 'Querobamba', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050902', 'Belén', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050903', 'Chalcos', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050904', 'Chilcayoc', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050905', 'Huacaña', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050906', 'Morcolla', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050907', 'Paico', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050908', 'San Pedro de Larcay', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050909', 'San Salvador de Quije', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050910', 'Santiago de Paucaray', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('050911', 'Soras', '0509', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051001', 'Huancapi', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051002', 'Alcamenca', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051003', 'Apongo', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051004', 'Asquipata', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051005', 'Canaria', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051006', 'Cayara', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051007', 'Colca', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051008', 'Huamanquiquia', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051009', 'Huancaraylla', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051010', 'Hualla', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051011', 'Sarhua', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051012', 'Vilcanchos', '0510', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051101', 'Vilcas Huaman', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051102', 'Accomarca', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051103', 'Carhuanca', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051104', 'Concepción', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051105', 'Huambalpa', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051106', 'Independencia', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051107', 'Saurama', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('051108', 'Vischongo', '0511', '05', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060101', 'Cajamarca', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060102', 'Asunción', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060103', 'Chetilla', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060104', 'Cospan', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060105', 'Encañada', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060106', 'Jesús', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060107', 'Llacanora', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060108', 'Los Baños del Inca', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060109', 'Magdalena', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060110', 'Matara', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060111', 'Namora', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060112', 'San Juan', '0601', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060201', 'Cajabamba', '0602', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060202', 'Cachachi', '0602', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060203', 'Condebamba', '0602', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060204', 'Sitacocha', '0602', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060301', 'Celendín', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060302', 'Chumuch', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060303', 'Cortegana', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060304', 'Huasmin', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060305', 'Jorge Chávez', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060306', 'José Gálvez', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060307', 'Miguel Iglesias', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060308', 'Oxamarca', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060309', 'Sorochuco', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060310', 'Sucre', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060311', 'Utco', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060312', 'La Libertad de Pallan', '0603', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060401', 'Chota', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060402', 'Anguia', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060403', 'Chadin', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060404', 'Chiguirip', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060405', 'Chimban', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060406', 'Choropampa', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060407', 'Cochabamba', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060408', 'Conchan', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060409', 'Huambos', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060410', 'Lajas', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060411', 'Llama', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060412', 'Miracosta', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060413', 'Paccha', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060414', 'Pion', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060415', 'Querocoto', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060416', 'San Juan de Licupis', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060417', 'Tacabamba', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060418', 'Tocmoche', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060419', 'Chalamarca', '0604', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060501', 'Contumaza', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060502', 'Chilete', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060503', 'Cupisnique', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060504', 'Guzmango', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060505', 'San Benito', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060506', 'Santa Cruz de Toledo', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060507', 'Tantarica', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060508', 'Yonan', '0605', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060601', 'Cutervo', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060602', 'Callayuc', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060603', 'Choros', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060604', 'Cujillo', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060605', 'La Ramada', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060606', 'Pimpingos', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060607', 'Querocotillo', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060608', 'San Andrés de Cutervo', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060609', 'San Juan de Cutervo', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060610', 'San Luis de Lucma', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060611', 'Santa Cruz', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060612', 'Santo Domingo de la Capilla', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060613', 'Santo Tomas', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060614', 'Socota', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060615', 'Toribio Casanova', '0606', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060701', 'Bambamarca', '0607', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060702', 'Chugur', '0607', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060703', 'Hualgayoc', '0607', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060801', 'Jaén', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060802', 'Bellavista', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060803', 'Chontali', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060804', 'Colasay', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060805', 'Huabal', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060806', 'Las Pirias', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060807', 'Pomahuaca', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060808', 'Pucara', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060809', 'Sallique', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060810', 'San Felipe', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060811', 'San José del Alto', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060812', 'Santa Rosa', '0608', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060901', 'San Ignacio', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060902', 'Chirinos', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060903', 'Huarango', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060904', 'La Coipa', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060905', 'Namballe', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060906', 'San José de Lourdes', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('060907', 'Tabaconas', '0609', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061001', 'Pedro Gálvez', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061002', 'Chancay', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061003', 'Eduardo Villanueva', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061004', 'Gregorio Pita', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061005', 'Ichocan', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061006', 'José Manuel Quiroz', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061007', 'José Sabogal', '0610', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061101', 'San Miguel', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061102', 'Bolívar', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061103', 'Calquis', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061104', 'Catilluc', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061105', 'El Prado', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061106', 'La Florida', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061107', 'Llapa', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061108', 'Nanchoc', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061109', 'Niepos', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061110', 'San Gregorio', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061111', 'San Silvestre de Cochan', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061112', 'Tongod', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061113', 'Unión Agua Blanca', '0611', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061201', 'San Pablo', '0612', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061202', 'San Bernardino', '0612', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061203', 'San Luis', '0612', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061204', 'Tumbaden', '0612', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061301', 'Santa Cruz', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061302', 'Andabamba', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061303', 'Catache', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061304', 'Chancaybaños', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061305', 'La Esperanza', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061306', 'Ninabamba', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061307', 'Pulan', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061308', 'Saucepampa', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061309', 'Sexi', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061310', 'Uticyacu', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('061311', 'Yauyucan', '0613', '06', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070101', 'Callao', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070102', 'Bellavista', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070103', 'Carmen de la Legua Reynoso', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070104', 'La Perla', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070105', 'La Punta', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070106', 'Ventanilla', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('070107', 'Mi Perú', '0701', '07', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080101', 'Cusco', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080102', 'Ccorca', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080103', 'Poroy', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080104', 'San Jerónimo', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080105', 'San Sebastian', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080106', 'Santiago', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080107', 'Saylla', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080108', 'Wanchaq', '0801', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080201', 'Acomayo', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080202', 'Acopia', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080203', 'Acos', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080204', 'Mosoc Llacta', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080205', 'Pomacanchi', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080206', 'Rondocan', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080207', 'Sangarara', '0802', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080301', 'Anta', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080302', 'Ancahuasi', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080303', 'Cachimayo', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080304', 'Chinchaypujio', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080305', 'Huarocondo', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080306', 'Limatambo', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080307', 'Mollepata', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080308', 'Pucyura', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080309', 'Zurite', '0803', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080401', 'Calca', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080402', 'Coya', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080403', 'Lamay', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080404', 'Lares', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080405', 'Pisac', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080406', 'San Salvador', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080407', 'Taray', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080408', 'Yanatile', '0804', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080501', 'Yanaoca', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080502', 'Checca', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080503', 'Kunturkanki', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080504', 'Langui', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080505', 'Layo', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080506', 'Pampamarca', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080507', 'Quehue', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080508', 'Tupac Amaru', '0805', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080601', 'Sicuani', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080602', 'Checacupe', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080603', 'Combapata', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080604', 'Marangani', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080605', 'Pitumarca', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080606', 'San Pablo', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080607', 'San Pedro', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080608', 'Tinta', '0806', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080701', 'Santo Tomas', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080702', 'Capacmarca', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080703', 'Chamaca', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080704', 'Colquemarca', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080705', 'Livitaca', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080706', 'Llusco', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080707', 'Quiñota', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080708', 'Velille', '0807', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080801', 'Espinar', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080802', 'Condoroma', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080803', 'Coporaque', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080804', 'Ocoruro', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080805', 'Pallpata', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080806', 'Pichigua', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080807', 'Suyckutambo', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080808', 'Alto Pichigua', '0808', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080901', 'Santa Ana', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080902', 'Echarate', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080903', 'Huayopata', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080904', 'Maranura', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080905', 'Ocobamba', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080906', 'Quellouno', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080907', 'Kimbiri', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080908', 'Santa Teresa', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080909', 'Vilcabamba', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080910', 'Pichari', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080911', 'Inkawasi', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080912', 'Villa Virgen', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080913', 'Villa Kintiarina', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('080914', 'Megantoni', '0809', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081001', 'Paruro', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081002', 'Accha', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081003', 'Ccapi', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081004', 'Colcha', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081005', 'Huanoquite', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081006', 'Omachaç', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081007', 'Paccaritambo', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081008', 'Pillpinto', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081009', 'Yaurisque', '0810', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081101', 'Paucartambo', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081102', 'Caicay', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081103', 'Challabamba', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081104', 'Colquepata', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081105', 'Huancarani', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081106', 'Kosñipata', '0811', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081201', 'Urcos', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081202', 'Andahuaylillas', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081203', 'Camanti', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081204', 'Ccarhuayo', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081205', 'Ccatca', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081206', 'Cusipata', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081207', 'Huaro', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081208', 'Lucre', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081209', 'Marcapata', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081210', 'Ocongate', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081211', 'Oropesa', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081212', 'Quiquijana', '0812', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081301', 'Urubamba', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081302', 'Chinchero', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081303', 'Huayllabamba', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081304', 'Machupicchu', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081305', 'Maras', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081306', 'Ollantaytambo', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('081307', 'Yucay', '0813', '08', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090101', 'Huancavelica', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090102', 'Acobambilla', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090103', 'Acoria', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090104', 'Conayca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090105', 'Cuenca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090106', 'Huachocolpa', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090107', 'Huayllahuara', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090108', 'Izcuchaca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090109', 'Laria', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090110', 'Manta', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090111', 'Mariscal Cáceres', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090112', 'Moya', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090113', 'Nuevo Occoro', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090114', 'Palca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090115', 'Pilchaca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090116', 'Vilca', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090117', 'Yauli', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090118', 'Ascensión', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090119', 'Huando', '0901', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090201', 'Acobamba', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090202', 'Andabamba', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090203', 'Anta', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090204', 'Caja', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090205', 'Marcas', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090206', 'Paucara', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090207', 'Pomacocha', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090208', 'Rosario', '0902', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090301', 'Lircay', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090302', 'Anchonga', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090303', 'Callanmarca', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090304', 'Ccochaccasa', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090305', 'Chincho', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090306', 'Congalla', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090307', 'Huanca-Huanca', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090308', 'Huayllay Grande', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090309', 'Julcamarca', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090310', 'San Antonio de Antaparco', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090311', 'Santo Tomas de Pata', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090312', 'Secclla', '0903', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090401', 'Castrovirreyna', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090402', 'Arma', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090403', 'Aurahua', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090404', 'Capillas', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090405', 'Chupamarca', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090406', 'Cocas', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090407', 'Huachos', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090408', 'Huamatambo', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090409', 'Mollepampa', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090410', 'San Juan', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090411', 'Santa Ana', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090412', 'Tantara', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090413', 'Ticrapo', '0904', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090501', 'Churcampa', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090502', 'Anco', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090503', 'Chinchihuasi', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090504', 'El Carmen', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090505', 'La Merced', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090506', 'Locroja', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090507', 'Paucarbamba', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090508', 'San Miguel de Mayocc', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090509', 'San Pedro de Coris', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090510', 'Pachamarca', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090511', 'Cosme', '0905', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090601', 'Huaytara', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090602', 'Ayavi', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090603', 'Córdova', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090604', 'Huayacundo Arma', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090605', 'Laramarca', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090606', 'Ocoyo', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090607', 'Pilpichaca', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090608', 'Querco', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090609', 'Quito-Arma', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090610', 'San Antonio de Cusicancha', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090611', 'San Francisco de Sangayaico', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090612', 'San Isidro', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090613', 'Santiago de Chocorvos', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090614', 'Santiago de Quirahuara', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090615', 'Santo Domingo de Capillas', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090616', 'Tambo', '0906', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090701', 'Pampas', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090702', 'Acostambo', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090703', 'Acraquia', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090704', 'Ahuaycha', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090705', 'Colcabamba', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090706', 'Daniel Hernández', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090707', 'Huachocolpa', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090709', 'Huaribamba', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090710', 'Ñahuimpuquio', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090711', 'Pazos', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090713', 'Quishuar', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090714', 'Salcabamba', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090715', 'Salcahuasi', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090716', 'San Marcos de Rocchac', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090717', 'Surcubamba', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090718', 'Tintay Puncu', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090719', 'Quichuas', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090720', 'Andaymarca', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090721', 'Roble', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090722', 'Pichos', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('090723', 'Santiago de Tucuma', '0907', '09', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100101', 'Huanuco', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100102', 'Amarilis', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100103', 'Chinchao', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100104', 'Churubamba', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100105', 'Margos', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100106', 'Quisqui (Kichki)', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100107', 'San Francisco de Cayran', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100108', 'San Pedro de Chaulan', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100109', 'Santa María del Valle', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100110', 'Yarumayo', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100111', 'Pillco Marca', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100112', 'Yacus', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100113', 'San Pablo de Pillao', '1001', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100201', 'Ambo', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100202', 'Cayna', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100203', 'Colpas', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100204', 'Conchamarca', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100205', 'Huacar', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100206', 'San Francisco', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100207', 'San Rafael', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100208', 'Tomay Kichwa', '1002', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100301', 'La Unión', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100307', 'Chuquis', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100311', 'Marías', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100313', 'Pachas', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100316', 'Quivilla', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100317', 'Ripan', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100321', 'Shunqui', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100322', 'Sillapata', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100323', 'Yanas', '1003', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100401', 'Huacaybamba', '1004', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100402', 'Canchabamba', '1004', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100403', 'Cochabamba', '1004', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100404', 'Pinra', '1004', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100501', 'Llata', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100502', 'Arancay', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100503', 'Chavín de Pariarca', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100504', 'Jacas Grande', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100505', 'Jircan', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100506', 'Miraflores', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100507', 'Monzón', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100508', 'Punchao', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100509', 'Puños', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100510', 'Singa', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100511', 'Tantamayo', '1005', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100601', 'Rupa-Rupa', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100602', 'Daniel Alomía Robles', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100603', 'Hermílio Valdizan', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100604', 'José Crespo y Castillo', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100605', 'Luyando', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100606', 'Mariano Damaso Beraun', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100607', 'Pucayacu', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100608', 'Castillo Grande', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100609', 'Pueblo Nuevo', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100610', 'Santo Domingo de Anda', '1006', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100701', 'Huacrachuco', '1007', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100702', 'Cholon', '1007', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100703', 'San Buenaventura', '1007', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100704', 'La Morada', '1007', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100705', 'Santa Rosa de Alto Yanajanca', '1007', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100801', 'Panao', '1008', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100802', 'Chaglla', '1008', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100803', 'Molino', '1008', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100804', 'Umari', '1008', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100901', 'Puerto Inca', '1009', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100902', 'Codo del Pozuzo', '1009', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100903', 'Honoria', '1009', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100904', 'Tournavista', '1009', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('100905', 'Yuyapichis', '1009', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101001', 'Jesús', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101002', 'Baños', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101003', 'Jivia', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101004', 'Queropalca', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101005', 'Rondos', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101006', 'San Francisco de Asís', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101007', 'San Miguel de Cauri', '1010', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101101', 'Chavinillo', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101102', 'Cahuac', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101103', 'Chacabamba', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101104', 'Aparicio Pomares', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101105', 'Jacas Chico', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101106', 'Obas', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101107', 'Pampamarca', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('101108', 'Choras', '1011', '10', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110101', 'Ica', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110102', 'La Tinguiña', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110103', 'Los Aquijes', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110104', 'Ocucaje', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110105', 'Pachacutec', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110106', 'Parcona', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110107', 'Pueblo Nuevo', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110108', 'Salas', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110109', 'San José de Los Molinos', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110110', 'San Juan Bautista', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110111', 'Santiago', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110112', 'Subtanjalla', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110113', 'Tate', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110114', 'Yauca del Rosario', '1101', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110201', 'Chincha Alta', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110202', 'Alto Laran', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110203', 'Chavin', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110204', 'Chincha Baja', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110205', 'El Carmen', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110206', 'Grocio Prado', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110207', 'Pueblo Nuevo', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110208', 'San Juan de Yanac', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110209', 'San Pedro de Huacarpana', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110210', 'Sunampe', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110211', 'Tambo de Mora', '1102', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110301', 'Nasca', '1103', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110302', 'Changuillo', '1103', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110303', 'El Ingenio', '1103', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110304', 'Marcona', '1103', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110305', 'Vista Alegre', '1103', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110401', 'Palpa', '1104', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110402', 'Llipata', '1104', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110403', 'Río Grande', '1104', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110404', 'Santa Cruz', '1104', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110405', 'Tibillo', '1104', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110501', 'Pisco', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110502', 'Huancano', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110503', 'Humay', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110504', 'Independencia', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110505', 'Paracas', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110506', 'San Andrés', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110507', 'San Clemente', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('110508', 'Tupac Amaru Inca', '1105', '11', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120101', 'Huancayo', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120104', 'Carhuacallanga', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120105', 'Chacapampa', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120106', 'Chicche', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120107', 'Chilca', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120108', 'Chongos Alto', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120111', 'Chupuro', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120112', 'Colca', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120113', 'Cullhuas', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120114', 'El Tambo', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120116', 'Huacrapuquio', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120117', 'Hualhuas', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120119', 'Huancan', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120120', 'Huasicancha', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120121', 'Huayucachi', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120122', 'Ingenio', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120124', 'Pariahuanca', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120125', 'Pilcomayo', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120126', 'Pucara', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120127', 'Quichuay', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120128', 'Quilcas', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120129', 'San Agustín', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120130', 'San Jerónimo de Tunan', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120132', 'Saño', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120133', 'Sapallanga', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120134', 'Sicaya', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120135', 'Santo Domingo de Acobamba', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120136', 'Viques', '1201', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120201', 'Concepción', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120202', 'Aco', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120203', 'Andamarca', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120204', 'Chambara', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120205', 'Cochas', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120206', 'Comas', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120207', 'Heroínas Toledo', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120208', 'Manzanares', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120209', 'Mariscal Castilla', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120210', 'Matahuasi', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120211', 'Mito', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120212', 'Nueve de Julio', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120213', 'Orcotuna', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120214', 'San José de Quero', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120215', 'Santa Rosa de Ocopa', '1202', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120301', 'Chanchamayo', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120302', 'Perene', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120303', 'Pichanaqui', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120304', 'San Luis de Shuaro', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120305', 'San Ramón', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120306', 'Vitoc', '1203', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120401', 'Jauja', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120402', 'Acolla', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120403', 'Apata', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120404', 'Ataura', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120405', 'Canchayllo', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120406', 'Curicaca', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120407', 'El Mantaro', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120408', 'Huamali', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120409', 'Huaripampa', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120410', 'Huertas', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120411', 'Janjaillo', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120412', 'Julcán', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120413', 'Leonor Ordóñez', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120414', 'Llocllapampa', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120415', 'Marco', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120416', 'Masma', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120417', 'Masma Chicche', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120418', 'Molinos', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120419', 'Monobamba', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120420', 'Muqui', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120421', 'Muquiyauyo', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120422', 'Paca', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120423', 'Paccha', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120424', 'Pancan', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120425', 'Parco', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120426', 'Pomacancha', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120427', 'Ricran', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120428', 'San Lorenzo', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120429', 'San Pedro de Chunan', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120430', 'Sausa', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120431', 'Sincos', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120432', 'Tunan Marca', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120433', 'Yauli', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120434', 'Yauyos', '1204', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120501', 'Junin', '1205', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120502', 'Carhuamayo', '1205', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120503', 'Ondores', '1205', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120504', 'Ulcumayo', '1205', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120601', 'Satipo', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120602', 'Coviriali', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120603', 'Llaylla', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120604', 'Mazamari', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120605', 'Pampa Hermosa', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120606', 'Pangoa', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120607', 'Río Negro', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120608', 'Río Tambo', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120609', 'Vizcatan del Ene', '1206', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120701', 'Tarma', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120702', 'Acobamba', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120703', 'Huaricolca', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120704', 'Huasahuasi', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120705', 'La Unión', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120706', 'Palca', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120707', 'Palcamayo', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120708', 'San Pedro de Cajas', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120709', 'Tapo', '1207', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120801', 'La Oroya', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120802', 'Chacapalpa', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120803', 'Huay-Huay', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120804', 'Marcapomacocha', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120805', 'Morococha', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120806', 'Paccha', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120807', 'Santa Bárbara de Carhuacayan', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120808', 'Santa Rosa de Sacco', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120809', 'Suitucancha', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120810', 'Yauli', '1208', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120901', 'Chupaca', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120902', 'Ahuac', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120903', 'Chongos Bajo', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120904', 'Huachac', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120905', 'Huamancaca Chico', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120906', 'San Juan de Iscos', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120907', 'San Juan de Jarpa', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120908', 'Tres de Diciembre', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('120909', 'Yanacancha', '1209', '12', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130101', 'Trujillo', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130102', 'El Porvenir', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130103', 'Florencia de Mora', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130104', 'Huanchaco', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130105', 'La Esperanza', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130106', 'Laredo', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130107', 'Moche', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130108', 'Poroto', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130109', 'Salaverry', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130110', 'Simbal', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130111', 'Victor Larco Herrera', '1301', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130201', 'Ascope', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130202', 'Chicama', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130203', 'Chocope', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130204', 'Magdalena de Cao', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130205', 'Paijan', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130206', 'Rázuri', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130207', 'Santiago de Cao', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130208', 'Casa Grande', '1302', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130301', 'Bolívar', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130302', 'Bambamarca', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130303', 'Condormarca', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130304', 'Longotea', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130305', 'Uchumarca', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130306', 'Ucuncha', '1303', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130401', 'Chepen', '1304', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130402', 'Pacanga', '1304', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130403', 'Pueblo Nuevo', '1304', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130501', 'Julcan', '1305', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130502', 'Calamarca', '1305', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130503', 'Carabamba', '1305', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130504', 'Huaso', '1305', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130601', 'Otuzco', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130602', 'Agallpampa', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130604', 'Charat', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130605', 'Huaranchal', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130606', 'La Cuesta', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130608', 'Mache', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130610', 'Paranday', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130611', 'Salpo', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130613', 'Sinsicap', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130614', 'Usquil', '1306', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130701', 'San Pedro de Lloc', '1307', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130702', 'Guadalupe', '1307', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130703', 'Jequetepeque', '1307', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130704', 'Pacasmayo', '1307', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130705', 'San José', '1307', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130801', 'Tayabamba', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130802', 'Buldibuyo', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130803', 'Chillia', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130804', 'Huancaspata', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130805', 'Huaylillas', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130806', 'Huayo', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130807', 'Ongon', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130808', 'Parcoy', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130809', 'Pataz', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130810', 'Pias', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130811', 'Santiago de Challas', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130812', 'Taurija', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130813', 'Urpay', '1308', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130901', 'Huamachuco', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130902', 'Chugay', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130903', 'Cochorco', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130904', 'Curgos', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130905', 'Marcabal', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130906', 'Sanagoran', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130907', 'Sarin', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('130908', 'Sartimbamba', '1309', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131001', 'Santiago de Chuco', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131002', 'Angasmarca', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131003', 'Cachicadan', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131004', 'Mollebamba', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131005', 'Mollepata', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131006', 'Quiruvilca', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131007', 'Santa Cruz de Chuca', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131008', 'Sitabamba', '1310', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131101', 'Cascas', '1311', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131102', 'Lucma', '1311', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131103', 'Marmot', '1311', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131104', 'Sayapullo', '1311', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131201', 'Viru', '1312', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131202', 'Chao', '1312', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('131203', 'Guadalupito', '1312', '13', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140101', 'Chiclayo', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140102', 'Chongoyape', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140103', 'Eten', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140104', 'Eten Puerto', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140105', 'José Leonardo Ortiz', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140106', 'La Victoria', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140107', 'Lagunas', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140108', 'Monsefu', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140109', 'Nueva Arica', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140110', 'Oyotun', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140111', 'Picsi', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140112', 'Pimentel', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140113', 'Reque', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140114', 'Santa Rosa', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140115', 'Saña', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140116', 'Cayalti', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140117', 'Patapo', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140118', 'Pomalca', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140119', 'Pucala', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140120', 'Tuman', '1401', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140201', 'Ferreñafe', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140202', 'Cañaris', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140203', 'Incahuasi', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140204', 'Manuel Antonio Mesones Muro', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140205', 'Pitipo', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140206', 'Pueblo Nuevo', '1402', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140301', 'Lambayeque', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140302', 'Chochope', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140303', 'Illimo', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140304', 'Jayanca', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140305', 'Mochumi', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140306', 'Morrope', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140307', 'Motupe', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140308', 'Olmos', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140309', 'Pacora', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140310', 'Salas', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140311', 'San José', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('140312', 'Tucume', '1403', '14', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150101', 'Lima', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150102', 'Ancón', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150103', 'Ate', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150104', 'Barranco', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150105', 'Breña', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150106', 'Carabayllo', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150107', 'Chaclacayo', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150108', 'Chorrillos', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150109', 'Cieneguilla', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150110', 'Comas', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150111', 'El Agustino', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150112', 'Independencia', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150113', 'Jesús María', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150114', 'La Molina', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150115', 'La Victoria', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150116', 'Lince', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150117', 'Los Olivos', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150118', 'Lurigancho', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150119', 'Lurin', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150120', 'Magdalena del Mar', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150121', 'Pueblo Libre', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150122', 'Miraflores', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150123', 'Pachacamac', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150124', 'Pucusana', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150125', 'Puente Piedra', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150126', 'Punta Hermosa', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150127', 'Punta Negra', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150128', 'Rímac', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150129', 'San Bartolo', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150130', 'San Borja', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150131', 'San Isidro', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150132', 'San Juan de Lurigancho', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150133', 'San Juan de Miraflores', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150134', 'San Luis', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150135', 'San Martín de Porres', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150136', 'San Miguel', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150137', 'Santa Anita', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150138', 'Santa María del Mar', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150139', 'Santa Rosa', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150140', 'Santiago de Surco', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150141', 'Surquillo', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150142', 'Villa El Salvador', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150143', 'Villa María del Triunfo', '1501', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150201', 'Barranca', '1502', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150202', 'Paramonga', '1502', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150203', 'Pativilca', '1502', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150204', 'Supe', '1502', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150205', 'Supe Puerto', '1502', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150301', 'Cajatambo', '1503', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150302', 'Copa', '1503', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150303', 'Gorgor', '1503', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150304', 'Huancapon', '1503', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150305', 'Manas', '1503', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150401', 'Canta', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150402', 'Arahuay', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150403', 'Huamantanga', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150404', 'Huaros', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150405', 'Lachaqui', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150406', 'San Buenaventura', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150407', 'Santa Rosa de Quives', '1504', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150501', 'San Vicente de Cañete', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150502', 'Asia', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150503', 'Calango', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150504', 'Cerro Azul', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150505', 'Chilca', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150506', 'Coayllo', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150507', 'Imperial', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150508', 'Lunahuana', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150509', 'Mala', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150510', 'Nuevo Imperial', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150511', 'Pacaran', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150512', 'Quilmana', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150513', 'San Antonio', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150514', 'San Luis', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150515', 'Santa Cruz de Flores', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150516', 'Zúñiga', '1505', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150601', 'Huaral', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150602', 'Atavillos Alto', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150603', 'Atavillos Bajo', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150604', 'Aucallama', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150605', 'Chancay', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150606', 'Ihuari', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150607', 'Lampian', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150608', 'Pacaraos', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150609', 'San Miguel de Acos', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150610', 'Santa Cruz de Andamarca', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150611', 'Sumbilca', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150612', 'Veintisiete de Noviembre', '1506', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150701', 'Matucana', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150702', 'Antioquia', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150703', 'Callahuanca', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150704', 'Carampoma', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150705', 'Chicla', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150706', 'Cuenca', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150707', 'Huachupampa', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150708', 'Huanza', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150709', 'Huarochiri', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150710', 'Lahuaytambo', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150711', 'Langa', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150712', 'Laraos', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150713', 'Mariatana', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150714', 'Ricardo Palma', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150715', 'San Andrés de Tupicocha', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150716', 'San Antonio', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150717', 'San Bartolomé', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150718', 'San Damian', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150719', 'San Juan de Iris', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150720', 'San Juan de Tantaranche', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150721', 'San Lorenzo de Quinti', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150722', 'San Mateo', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150723', 'San Mateo de Otao', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150724', 'San Pedro de Casta', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150725', 'San Pedro de Huancayre', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150726', 'Sangallaya', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150727', 'Santa Cruz de Cocachacra', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150728', 'Santa Eulalia', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150729', 'Santiago de Anchucaya', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150730', 'Santiago de Tuna', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150731', 'Santo Domingo de Los Olleros', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150732', 'Surco', '1507', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150801', 'Huacho', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150802', 'Ambar', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150803', 'Caleta de Carquin', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150804', 'Checras', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150805', 'Hualmay', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150806', 'Huaura', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150807', 'Leoncio Prado', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150808', 'Paccho', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150809', 'Santa Leonor', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150810', 'Santa María', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150811', 'Sayan', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150812', 'Vegueta', '1508', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150901', 'Oyon', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150902', 'Andajes', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150903', 'Caujul', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150904', 'Cochamarca', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150905', 'Navan', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('150906', 'Pachangara', '1509', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151001', 'Yauyos', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151002', 'Alis', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151003', 'Allauca', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151004', 'Ayaviri', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151005', 'Azángaro', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151006', 'Cacra', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151007', 'Carania', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151008', 'Catahuasi', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151009', 'Chocos', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151010', 'Cochas', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151011', 'Colonia', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151012', 'Hongos', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151013', 'Huampara', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151014', 'Huancaya', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151015', 'Huangascar', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151016', 'Huantan', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151017', 'Huañec', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151018', 'Laraos', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151019', 'Lincha', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151020', 'Madean', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151021', 'Miraflores', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151022', 'Omas', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151023', 'Putinza', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151024', 'Quinches', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151025', 'Quinocay', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151026', 'San Joaquín', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151027', 'San Pedro de Pilas', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151028', 'Tanta', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151029', 'Tauripampa', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151030', 'Tomas', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151031', 'Tupe', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151032', 'Viñac', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('151033', 'Vitis', '1510', '15', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160101', 'Iquitos', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160102', 'Alto Nanay', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160103', 'Fernando Lores', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160104', 'Indiana', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160105', 'Las Amazonas', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160106', 'Mazan', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160107', 'Napo', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160108', 'Punchana', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160110', 'Torres Causana', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160112', 'Belén', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160113', 'San Juan Bautista', '1601', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160201', 'Yurimaguas', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160202', 'Balsapuerto', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160205', 'Jeberos', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160206', 'Lagunas', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160210', 'Santa Cruz', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160211', 'Teniente Cesar López Rojas', '1602', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160301', 'Nauta', '1603', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160302', 'Parinari', '1603', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160303', 'Tigre', '1603', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160304', 'Trompeteros', '1603', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160305', 'Urarinas', '1603', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160401', 'Ramón Castilla', '1604', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160402', 'Pebas', '1604', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160403', 'Yavari', '1604', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160404', 'San Pablo', '1604', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160501', 'Requena', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160502', 'Alto Tapiche', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160503', 'Capelo', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160504', 'Emilio San Martín', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160505', 'Maquia', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160506', 'Puinahua', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160507', 'Saquena', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160508', 'Soplin', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160509', 'Tapiche', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160510', 'Jenaro Herrera', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160511', 'Yaquerana', '1605', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160601', 'Contamana', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160602', 'Inahuaya', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160603', 'Padre Márquez', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160604', 'Pampa Hermosa', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160605', 'Sarayacu', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160606', 'Vargas Guerra', '1606', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160701', 'Barranca', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160702', 'Cahuapanas', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160703', 'Manseriche', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160704', 'Morona', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160705', 'Pastaza', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160706', 'Andoas', '1607', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160801', 'Putumayo', '1608', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160802', 'Rosa Panduro', '1608', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160803', 'Teniente Manuel Clavero', '1608', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('160804', 'Yaguas', '1608', '16', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170101', 'Tambopata', '1701', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170102', 'Inambari', '1701', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170103', 'Las Piedras', '1701', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170104', 'Laberinto', '1701', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170201', 'Manu', '1702', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170202', 'Fitzcarrald', '1702', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170203', 'Madre de Dios', '1702', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170204', 'Huepetuhe', '1702', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170301', 'Iñapari', '1703', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170302', 'Iberia', '1703', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('170303', 'Tahuamanu', '1703', '17', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180101', 'Moquegua', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180102', 'Carumas', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180103', 'Cuchumbaya', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180104', 'Samegua', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180105', 'San Cristóbal', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180106', 'Torata', '1801', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180201', 'Omate', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180202', 'Chojata', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180203', 'Coalaque', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180204', 'Ichuña', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180205', 'La Capilla', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180206', 'Lloque', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180207', 'Matalaque', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180208', 'Puquina', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180209', 'Quinistaquillas', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180210', 'Ubinas', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180211', 'Yunga', '1802', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180301', 'Ilo', '1803', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180302', 'El Algarrobal', '1803', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('180303', 'Pacocha', '1803', '18', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190101', 'Chaupimarca', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190102', 'Huachon', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190103', 'Huariaca', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190104', 'Huayllay', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190105', 'Ninacaca', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190106', 'Pallanchacra', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190107', 'Paucartambo', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190108', 'San Francisco de Asís de Yarusyacan', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190109', 'Simon Bolívar', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190110', 'Ticlacayan', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190111', 'Tinyahuarco', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190112', 'Vicco', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190113', 'Yanacancha', '1901', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190201', 'Yanahuanca', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190202', 'Chacayan', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190203', 'Goyllarisquizga', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190204', 'Paucar', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190205', 'San Pedro de Pillao', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190206', 'Santa Ana de Tusi', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190207', 'Tapuc', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190208', 'Vilcabamba', '1902', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190301', 'Oxapampa', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190302', 'Chontabamba', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190303', 'Huancabamba', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190304', 'Palcazu', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190305', 'Pozuzo', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190306', 'Puerto Bermúdez', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190307', 'Villa Rica', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('190308', 'Constitución', '1903', '19', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200101', 'Piura', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200104', 'Castilla', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200105', 'Catacaos', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200107', 'Cura Mori', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200108', 'El Tallan', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200109', 'La Arena', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200110', 'La Unión', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200111', 'Las Lomas', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200114', 'Tambo Grande', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200115', 'Veintiseis de Octubre', '2001', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200201', 'Ayabaca', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200202', 'Frias', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200203', 'Jilili', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200204', 'Lagunas', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200205', 'Montero', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200206', 'Pacaipampa', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200207', 'Paimas', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200208', 'Sapillica', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200209', 'Sicchez', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200210', 'Suyo', '2002', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200301', 'Huancabamba', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200302', 'Canchaque', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200303', 'El Carmen de la Frontera', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200304', 'Huarmaca', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200305', 'Lalaquiz', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200306', 'San Miguel de El Faique', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200307', 'Sondor', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200308', 'Sondorillo', '2003', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200401', 'Chulucanas', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200402', 'Buenos Aires', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200403', 'Chalaco', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200404', 'La Matanza', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200405', 'Morropon', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200406', 'Salitral', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200407', 'San Juan de Bigote', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200408', 'Santa Catalina de Mossa', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200409', 'Santo Domingo', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200410', 'Yamango', '2004', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200501', 'Paita', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200502', 'Amotape', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200503', 'Arenal', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200504', 'Colan', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200505', 'La Huaca', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200506', 'Tamarindo', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200507', 'Vichayal', '2005', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200601', 'Sullana', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200602', 'Bellavista', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200603', 'Ignacio Escudero', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200604', 'Lancones', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200605', 'Marcavelica', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200606', 'Miguel Checa', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200607', 'Querecotillo', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200608', 'Salitral', '2006', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200701', 'Pariñas', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200702', 'El Alto', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200703', 'La Brea', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200704', 'Lobitos', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200705', 'Los Organos', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200706', 'Mancora', '2007', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200801', 'Sechura', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200802', 'Bellavista de la Unión', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200803', 'Bernal', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200804', 'Cristo Nos Valga', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200805', 'Vice', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('200806', 'Rinconada Llicuar', '2008', '20', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210101', 'Puno', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210102', 'Acora', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210103', 'Amantani', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210104', 'Atuncolla', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210105', 'Capachica', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210106', 'Chucuito', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210107', 'Coata', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210108', 'Huata', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210109', 'Mañazo', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210110', 'Paucarcolla', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210111', 'Pichacani', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210112', 'Plateria', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210113', 'San Antonio', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210114', 'Tiquillaca', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210115', 'Vilque', '2101', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210201', 'Azángaro', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210202', 'Achaya', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210203', 'Arapa', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210204', 'Asillo', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210205', 'Caminaca', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210206', 'Chupa', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210207', 'José Domingo Choquehuanca', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210208', 'Muñani', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210209', 'Potoni', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210210', 'Saman', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210211', 'San Anton', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210212', 'San José', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210213', 'San Juan de Salinas', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210214', 'Santiago de Pupuja', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210215', 'Tirapata', '2102', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210301', 'Macusani', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210302', 'Ajoyani', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210303', 'Ayapata', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210304', 'Coasa', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210305', 'Corani', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210306', 'Crucero', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210307', 'Ituata', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210308', 'Ollachea', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210309', 'San Gaban', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210310', 'Usicayos', '2103', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210401', 'Juli', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210402', 'Desaguadero', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210403', 'Huacullani', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210404', 'Kelluyo', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210405', 'Pisacoma', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210406', 'Pomata', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210407', 'Zepita', '2104', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210501', 'Ilave', '2105', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210502', 'Capazo', '2105', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210503', 'Pilcuyo', '2105', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210504', 'Santa Rosa', '2105', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210505', 'Conduriri', '2105', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210601', 'Huancane', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210602', 'Cojata', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210603', 'Huatasani', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210604', 'Inchupalla', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210605', 'Pusi', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210606', 'Rosaspata', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210607', 'Taraco', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210608', 'Vilque Chico', '2106', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210701', 'Lampa', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210702', 'Cabanilla', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210703', 'Calapuja', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210704', 'Nicasio', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210705', 'Ocuviri', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210706', 'Palca', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210707', 'Paratia', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210708', 'Pucara', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210709', 'Santa Lucia', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210710', 'Vilavila', '2107', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210801', 'Ayaviri', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210802', 'Antauta', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210803', 'Cupi', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210804', 'Llalli', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210805', 'Macari', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210806', 'Nuñoa', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210807', 'Orurillo', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210808', 'Santa Rosa', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210809', 'Umachiri', '2108', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210901', 'Moho', '2109', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210902', 'Conima', '2109', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210903', 'Huayrapata', '2109', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('210904', 'Tilali', '2109', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211001', 'Putina', '2110', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211002', 'Ananea', '2110', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211003', 'Pedro Vilca Apaza', '2110', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211004', 'Quilcapuncu', '2110', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211005', 'Sina', '2110', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211101', 'Juliaca', '2111', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211102', 'Cabana', '2111', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211103', 'Cabanillas', '2111', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211104', 'Caracoto', '2111', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211105', 'San Miguel', '2111', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211201', 'Sandia', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211202', 'Cuyocuyo', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211203', 'Limbani', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211204', 'Patambuco', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211205', 'Phara', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211206', 'Quiaca', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211207', 'San Juan del Oro', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211208', 'Yanahuaya', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211209', 'Alto Inambari', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211210', 'San Pedro de Putina Punco', '2112', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211301', 'Yunguyo', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211302', 'Anapia', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211303', 'Copani', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211304', 'Cuturapi', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211305', 'Ollaraya', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211306', 'Tinicachi', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('211307', 'Unicachi', '2113', '21', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220101', 'Moyobamba', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220102', 'Calzada', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220103', 'Habana', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220104', 'Jepelacio', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220105', 'Soritor', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220106', 'Yantalo', '2201', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220201', 'Bellavista', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220202', 'Alto Biavo', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220203', 'Bajo Biavo', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220204', 'Huallaga', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220205', 'San Pablo', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220206', 'San Rafael', '2202', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220301', 'San José de Sisa', '2203', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220302', 'Agua Blanca', '2203', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220303', 'San Martín', '2203', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220304', 'Santa Rosa', '2203', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220305', 'Shatoja', '2203', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220401', 'Saposoa', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220402', 'Alto Saposoa', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220403', 'El Eslabón', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220404', 'Piscoyacu', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220405', 'Sacanche', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220406', 'Tingo de Saposoa', '2204', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220501', 'Lamas', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220502', 'Alonso de Alvarado', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220503', 'Barranquita', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220504', 'Caynarachi', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220505', 'Cuñumbuqui', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220506', 'Pinto Recodo', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220507', 'Rumisapa', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220508', 'San Roque de Cumbaza', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220509', 'Shanao', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220510', 'Tabalosos', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220511', 'Zapatero', '2205', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220601', 'Juanjuí', '2206', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220602', 'Campanilla', '2206', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220603', 'Huicungo', '2206', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220604', 'Pachiza', '2206', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220605', 'Pajarillo', '2206', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220701', 'Picota', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220702', 'Buenos Aires', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220703', 'Caspisapa', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220704', 'Pilluana', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220705', 'Pucacaca', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220706', 'San Cristóbal', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220707', 'San Hilarión', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220708', 'Shamboyacu', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220709', 'Tingo de Ponasa', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220710', 'Tres Unidos', '2207', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220801', 'Rioja', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220802', 'Awajun', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220803', 'Elías Soplin Vargas', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220804', 'Nueva Cajamarca', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220805', 'Pardo Miguel', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220806', 'Posic', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220807', 'San Fernando', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220808', 'Yorongos', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220809', 'Yuracyacu', '2208', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220901', 'Tarapoto', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220902', 'Alberto Leveau', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220903', 'Cacatachi', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220904', 'Chazuta', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220905', 'Chipurana', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220906', 'El Porvenir', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220907', 'Huimbayoc', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220908', 'Juan Guerra', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220909', 'La Banda de Shilcayo', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220910', 'Morales', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220911', 'Papaplaya', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220912', 'San Antonio', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220913', 'Sauce', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('220914', 'Shapaja', '2209', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('221001', 'Tocache', '2210', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('221002', 'Nuevo Progreso', '2210', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('221003', 'Polvora', '2210', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('221004', 'Shunte', '2210', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('221005', 'Uchiza', '2210', '22', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230101', 'Tacna', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230102', 'Alto de la Alianza', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230103', 'Calana', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230104', 'Ciudad Nueva', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230105', 'Inclan', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230106', 'Pachia', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230107', 'Palca', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230108', 'Pocollay', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230109', 'Sama', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230110', 'Coronel Gregorio Albarracín Lanchipa', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230111', 'La Yarada los Palos', '2301', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230201', 'Candarave', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230202', 'Cairani', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230203', 'Camilaca', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230204', 'Curibaya', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230205', 'Huanuara', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230206', 'Quilahuani', '2302', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230301', 'Locumba', '2303', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230302', 'Ilabaya', '2303', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230303', 'Ite', '2303', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230401', 'Tarata', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230402', 'Héroes Albarracín', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230403', 'Estique', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230404', 'Estique-Pampa', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230405', 'Sitajara', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230406', 'Susapaya', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230407', 'Tarucachi', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('230408', 'Ticaco', '2304', '23', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240101', 'Tumbes', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240102', 'Corrales', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240103', 'La Cruz', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240104', 'Pampas de Hospital', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240105', 'San Jacinto', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240106', 'San Juan de la Virgen', '2401', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240201', 'Zorritos', '2402', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240202', 'Casitas', '2402', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240203', 'Canoas de Punta Sal', '2402', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240301', 'Zarumilla', '2403', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240302', 'Aguas Verdes', '2403', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240303', 'Matapalo', '2403', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('240304', 'Papayal', '2403', '24', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250101', 'Calleria', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250102', 'Campoverde', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250103', 'Iparia', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250104', 'Masisea', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250105', 'Yarinacocha', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250106', 'Nueva Requena', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250107', 'Manantay', '2501', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250201', 'Raymondi', '2502', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250202', 'Sepahua', '2502', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250203', 'Tahuania', '2502', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250204', 'Yurua', '2502', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250301', 'Padre Abad', '2503', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250302', 'Irazola', '2503', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250303', 'Curimana', '2503', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250304', 'Neshuya', '2503', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250305', 'Alexander Von Humboldt', '2503', '25', 'PE');
INSERT INTO `ubigeo_peru_districts` VALUES ('250401', 'Purus', '2504', '25', 'PE');

-- ----------------------------
-- Table structure for ubigeo_peru_provinces
-- ----------------------------
DROP TABLE IF EXISTS `ubigeo_peru_provinces`;
CREATE TABLE `ubigeo_peru_provinces`  (
  `id` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `id_pais` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `id_department` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ubigeo_peru_provinces
-- ----------------------------
INSERT INTO `ubigeo_peru_provinces` VALUES ('0101', 'Chachapoyas', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0102', 'Bagua', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0103', 'Bongará', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0104', 'Condorcanqui', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0105', 'Luya', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0106', 'Rodríguez de Mendoza', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0107', 'Utcubamba', 'PE', '01');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0201', 'Huaraz', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0202', 'Aija', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0203', 'Antonio Raymondi', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0204', 'Asunción', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0205', 'Bolognesi', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0206', 'Carhuaz', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0207', 'Carlos Fermín Fitzcarrald', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0208', 'Casma', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0209', 'Corongo', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0210', 'Huari', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0211', 'Huarmey', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0212', 'Huaylas', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0213', 'Mariscal Luzuriaga', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0214', 'Ocros', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0215', 'Pallasca', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0216', 'Pomabamba', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0217', 'Recuay', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0218', 'Santa', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0219', 'Sihuas', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0220', 'Yungay', 'PE', '02');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0301', 'Abancay', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0302', 'Andahuaylas', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0303', 'Antabamba', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0304', 'Aymaraes', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0305', 'Cotabambas', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0306', 'Chincheros', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0307', 'Grau', 'PE', '03');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0401', 'Arequipa', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0402', 'Camaná', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0403', 'Caravelí', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0404', 'Castilla', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0405', 'Caylloma', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0406', 'Condesuyos', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0407', 'Islay', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0408', 'La Uniòn', 'PE', '04');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0501', 'Huamanga', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0502', 'Cangallo', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0503', 'Huanca Sancos', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0504', 'Huanta', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0505', 'La Mar', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0506', 'Lucanas', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0507', 'Parinacochas', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0508', 'Pàucar del Sara Sara', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0509', 'Sucre', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0510', 'Víctor Fajardo', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0511', 'Vilcas Huamán', 'PE', '05');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0601', 'Cajamarca', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0602', 'Cajabamba', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0603', 'Celendín', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0604', 'Chota', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0605', 'Contumazá', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0606', 'Cutervo', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0607', 'Hualgayoc', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0608', 'Jaén', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0609', 'San Ignacio', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0610', 'San Marcos', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0611', 'San Miguel', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0612', 'San Pablo', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0613', 'Santa Cruz', 'PE', '06');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0701', 'Prov. Const. del Callao', 'PE', '07');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0801', 'Cusco', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0802', 'Acomayo', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0803', 'Anta', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0804', 'Calca', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0805', 'Canas', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0806', 'Canchis', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0807', 'Chumbivilcas', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0808', 'Espinar', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0809', 'La Convención', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0810', 'Paruro', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0811', 'Paucartambo', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0812', 'Quispicanchi', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0813', 'Urubamba', 'PE', '08');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0901', 'Huancavelica', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0902', 'Acobamba', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0903', 'Angaraes', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0904', 'Castrovirreyna', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0905', 'Churcampa', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0906', 'Huaytará', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('0907', 'Tayacaja', 'PE', '09');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1001', 'Huánuco', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1002', 'Ambo', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1003', 'Dos de Mayo', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1004', 'Huacaybamba', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1005', 'Huamalíes', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1006', 'Leoncio Prado', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1007', 'Marañón', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1008', 'Pachitea', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1009', 'Puerto Inca', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1010', 'Lauricocha ', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1011', 'Yarowilca ', 'PE', '10');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1101', 'Ica ', 'PE', '11');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1102', 'Chincha ', 'PE', '11');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1103', 'Nasca ', 'PE', '11');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1104', 'Palpa ', 'PE', '11');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1105', 'Pisco ', 'PE', '11');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1201', 'Huancayo ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1202', 'Concepción ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1203', 'Chanchamayo ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1204', 'Jauja ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1205', 'Junín ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1206', 'Satipo ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1207', 'Tarma ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1208', 'Yauli ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1209', 'Chupaca ', 'PE', '12');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1301', 'Trujillo ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1302', 'Ascope ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1303', 'Bolívar ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1304', 'Chepén ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1305', 'Julcán ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1306', 'Otuzco ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1307', 'Pacasmayo ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1308', 'Pataz ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1309', 'Sánchez Carrión ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1310', 'Santiago de Chuco ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1311', 'Gran Chimú ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1312', 'Virú ', 'PE', '13');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1401', 'Chiclayo ', 'PE', '14');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1402', 'Ferreñafe ', 'PE', '14');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1403', 'Lambayeque ', 'PE', '14');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1501', 'Lima ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1502', 'Barranca ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1503', 'Cajatambo ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1504', 'Canta ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1505', 'Cañete ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1506', 'Huaral ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1507', 'Huarochirí ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1508', 'Huaura ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1509', 'Oyón ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1510', 'Yauyos ', 'PE', '15');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1601', 'Maynas ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1602', 'Alto Amazonas ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1603', 'Loreto ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1604', 'Mariscal Ramón Castilla ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1605', 'Requena ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1606', 'Ucayali ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1607', 'Datem del Marañón ', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1608', 'Putumayo', 'PE', '16');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1701', 'Tambopata ', 'PE', '17');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1702', 'Manu ', 'PE', '17');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1703', 'Tahuamanu ', 'PE', '17');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1801', 'Mariscal Nieto ', 'PE', '18');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1802', 'General Sánchez Cerro ', 'PE', '18');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1803', 'Ilo ', 'PE', '18');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1901', 'Pasco ', 'PE', '19');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1902', 'Daniel Alcides Carrión ', 'PE', '19');
INSERT INTO `ubigeo_peru_provinces` VALUES ('1903', 'Oxapampa ', 'PE', '19');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2001', 'Piura ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2002', 'Ayabaca ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2003', 'Huancabamba ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2004', 'Morropón ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2005', 'Paita ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2006', 'Sullana ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2007', 'Talara ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2008', 'Sechura ', 'PE', '20');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2101', 'Puno ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2102', 'Azángaro ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2103', 'Carabaya ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2104', 'Chucuito ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2105', 'El Collao ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2106', 'Huancané ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2107', 'Lampa ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2108', 'Melgar ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2109', 'Moho ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2110', 'San Antonio de Putina ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2111', 'San Román ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2112', 'Sandia ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2113', 'Yunguyo ', 'PE', '21');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2201', 'Moyobamba ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2202', 'Bellavista ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2203', 'El Dorado ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2204', 'Huallaga ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2205', 'Lamas ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2206', 'Mariscal Cáceres ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2207', 'Picota ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2208', 'Rioja ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2209', 'San Martín ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2210', 'Tocache ', 'PE', '22');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2301', 'Tacna ', 'PE', '23');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2302', 'Candarave ', 'PE', '23');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2303', 'Jorge Basadre ', 'PE', '23');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2304', 'Tarata ', 'PE', '23');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2401', 'Tumbes ', 'PE', '24');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2402', 'Contralmirante Villar ', 'PE', '24');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2403', 'Zarumilla ', 'PE', '24');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2501', 'Coronel Portillo ', 'PE', '25');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2502', 'Atalaya ', 'PE', '25');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2503', 'Padre Abad ', 'PE', '25');
INSERT INTO `ubigeo_peru_provinces` VALUES ('2504', 'Purús', 'PE', '25');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_owner` int NULL DEFAULT NULL COMMENT 'Autor quien creo el usuario: d_usuario',
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `Activo` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'S' COMMENT 'Estado del usuario: S = Activo, N = Inactivo',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `users_email_unique`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'ADMIN', 'ADMIN@GMAIL.COM', NULL, NULL, '$2y$12$kdu7ICFRajq51RMJfCca6egFI4qTUzmAjdaQ9ijwoQHuknRb3/Uq2', NULL, '2024-11-03 11:35:05', '2026-01-28 08:50:33', 'S', 'avatar/1769590233.jpg');
INSERT INTO `users` VALUES (34, 'FLOR', 'FLOR@GMAIL.COM', NULL, NULL, '$2y$12$mzj8Py3azUebAPN9re75Xe.VUYR/Z.GGP1f0gNhjmsQm0Iho5QBJy', NULL, '2025-11-18 23:04:20', '2025-11-19 00:09:07', 'S', NULL);
INSERT INTO `users` VALUES (36, 'ANA', 'ANA@GMAIL.COM', NULL, NULL, '$2y$12$q4AwSzjX3Avs/Xbm6Na4DuTt.x2tfcOULjPpTIe0GlNcwAxZVSvSm', NULL, '2025-11-23 14:03:45', '2025-11-23 14:03:45', 'S', NULL);
INSERT INTO `users` VALUES (37, 'KATERIN', 'KATERIN@GMAIL.COM', NULL, NULL, '$2y$12$AppAJwMZ7arF1QhUe3F9E.tiGXKQQvOxyhZQlMPK9c2JyNObhTDUC', NULL, '2025-11-23 14:04:07', '2025-11-23 14:04:07', 'S', NULL);

-- ----------------------------
-- Function structure for SPLIT_STR
-- ----------------------------
DROP FUNCTION IF EXISTS `SPLIT_STR`;
delimiter ;;
CREATE FUNCTION `SPLIT_STR`(x VARCHAR(255),
  delim VARCHAR(12),
  pos INT)
 RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '')
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMIMNISTRACION_PROVEEDOR_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMIMNISTRACION_PROVEEDOR_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMIMNISTRACION_PROVEEDOR_LISTAR`()
BEGIN
    SELECT
        ROW_NUMBER() OVER (ORDER BY AE.id_proveedor) AS RowIndex,
        AE.id_proveedor,
        AE.nombre_empresa,
        AE.ruc,
        AE.direccion,
        AE.telefono,
        AE.email,
        AE.contacto_principal,
        AE.tipo_proveedor,
        AE.terminos_pago,
        AE.cuenta_bancaria,
        AE.sitio_web,
        AE.notas,
        AE.fecha_inicio,
        AE.calificacion,
        AE.Activo,
        AE.created_at,
        AE.updated_at
    FROM 
        administracion_proveedor AE 
    ORDER BY 
        AE.id_proveedor DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_ACTUALIZAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_ACTUALIZAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_ACTUALIZAR`(IN p_id_cliente INT,
    -- Parámetros del cliente
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_telefono VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_direccion VARCHAR(255),
    IN p_ruc VARCHAR(255),
    IN p_razon_social VARCHAR(255),
    IN p_fecha_nacimiento DATETIME,
    IN p_Activo CHAR(1),
    IN p_observacion TEXT,
    IN p_codigo_nip VARCHAR(255),
    IN p_ubigueo_pais VARCHAR(255),
    IN p_ubigueo_departamento VARCHAR(255),
    IN p_ubigueo_provincia VARCHAR(255),
    IN p_ubigueo_distrito VARCHAR(255),
    -- Parámetros de actividad
    IN p_id_ficha_actividad INT,
    IN p_tipo_compra VARCHAR(255),
    IN p_tipo_comunicacion VARCHAR(255),
    IN p_nombre_patrocinador VARCHAR(255),
    IN p_nombre_lider_ascendente VARCHAR(255),
    IN p_interes_principal VARCHAR(255),
		
	  IN p_descuento_promocion VARCHAR(255),
		IN p_id_nivel_promocion VARCHAR(255),
		
    -- Parámetros de seguimiento
    IN p_id_ficha_seguimiento INT,
    IN p_fecha_llamada TIMESTAMP,
    IN p_fecha_proxima TIMESTAMP,
    IN p_observacion_seguimiento TEXT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- 1. Actualizar tabla cliente
    UPDATE administracion_cliente
    SET
        nombre = p_nombre,
        apellido = p_apellido,
        telefono = p_telefono,
        email = p_email,
        direccion = p_direccion,
        ruc = p_ruc,
        razon_social = p_razon_social,
        fecha_nacimiento = p_fecha_nacimiento,
        Activo = p_Activo,
        observacion = p_observacion,
        codigo_nip = p_codigo_nip,
        ubigueo_pais = p_ubigueo_pais,
        ubigueo_departamento = p_ubigueo_departamento,
        ubigueo_provincia = p_ubigueo_provincia,
        ubigueo_distrito = p_ubigueo_distrito,
        updated_at = NOW()
    WHERE id_cliente = p_id_cliente;

    -- 2. Actualizar o insertar en tabla actividad
    IF p_id_ficha_actividad IS NULL OR p_id_ficha_actividad = 0 THEN
        INSERT INTO ficha_cliente_actividad (
            id_cliente, tipo_compra, tipo_comunicacion, nombre_patrocinador,
            nombre_lider_ascendente, interes_principal, created_at, updated_at
        ) VALUES (
            p_id_cliente, p_tipo_compra, p_tipo_comunicacion, p_nombre_patrocinador,
            p_nombre_lider_ascendente, p_interes_principal, NOW(), NOW()
        );
    ELSE
        UPDATE ficha_cliente_actividad
        SET
            tipo_compra = p_tipo_compra,
            tipo_comunicacion = p_tipo_comunicacion,
            nombre_patrocinador = p_nombre_patrocinador,
            nombre_lider_ascendente = p_nombre_lider_ascendente,
            interes_principal = p_interes_principal,
						
						descuento_promocion = p_descuento_promocion,
						id_nivel_promocion = p_id_nivel_promocion,
						
            updated_at = NOW()
        WHERE id_ficha_actividad = p_id_ficha_actividad;
    END IF;

    -- 3. Actualizar o insertar en tabla seguimiento SOLO SI hay datos
    IF p_id_ficha_seguimiento IS NULL OR p_id_ficha_seguimiento = 0 THEN
        -- Solo inserta si hay al menos algún dato de seguimiento
        IF (p_observacion_seguimiento IS NOT NULL AND TRIM(p_observacion_seguimiento) != '') 
           OR p_fecha_llamada IS NOT NULL 
           OR p_fecha_proxima IS NOT NULL THEN
            INSERT INTO ficha_cliente_seguimiento (
                id_cliente, fecha_llamada, fecha_proxima, observacion, created_at, updated_at
            ) VALUES (
                p_id_cliente, p_fecha_llamada, p_fecha_proxima, p_observacion_seguimiento, NOW(), NOW()
            );
        END IF;
    ELSE
        -- Si existe, actualizar
        UPDATE ficha_cliente_seguimiento
        SET
            fecha_llamada = p_fecha_llamada,
            fecha_proxima = p_fecha_proxima,
            observacion = p_observacion_seguimiento,
            updated_at = NOW()
        WHERE id_ficha_seguimiento = p_id_ficha_seguimiento;
    END IF;

    COMMIT;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_CREAR`(IN p_id_usuario INT,
    -- Parámetros del cliente
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_telefono VARCHAR(255),
		IN p_tipo_cliente CHAR(1),
    IN p_email VARCHAR(255),
    IN p_direccion VARCHAR(255),
    IN p_ruc VARCHAR(255),
    IN p_razon_social VARCHAR(255),
    IN p_fecha_nacimiento DATETIME,
    IN p_Activo CHAR(1),
    IN p_observacion TEXT,
    IN p_codigo_nip VARCHAR(255),
    IN p_ubigueo_pais VARCHAR(255),
    IN p_ubigueo_departamento VARCHAR(255),
    IN p_ubigueo_provincia VARCHAR(255),
    IN p_ubigueo_distrito VARCHAR(255),
    -- Parámetros de actividad
    IN p_tipo_compra VARCHAR(255),
    IN p_tipo_comunicacion VARCHAR(255),
    IN p_nombre_patrocinador VARCHAR(255),
    IN p_nombre_lider_ascendente VARCHAR(255),
    IN p_interes_principal VARCHAR(255),
		
		IN p_descuento_promocion VARCHAR(255),
		IN p_id_nivel_promocion VARCHAR(255),

    -- Parámetros de seguimiento
    IN p_fecha_llamada TIMESTAMP,
    IN p_fecha_proxima TIMESTAMP,
    IN p_observacion_seguimiento TEXT,
    OUT p_id_cliente_out INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- 1. Insertar en tabla cliente
    INSERT INTO administracion_cliente (
        id_usuario, nombre, apellido, telefono,tipo_cliente, email, direccion, ruc, razon_social,
        fecha_nacimiento, Activo, observacion, 
        codigo_nip, ubigueo_pais, ubigueo_departamento, ubigueo_provincia, ubigueo_distrito,
        created_at, updated_at
    ) VALUES (
        p_id_usuario, p_nombre, p_apellido, p_telefono,p_tipo_cliente, p_email, p_direccion, p_ruc, p_razon_social,
        p_fecha_nacimiento, p_Activo, p_observacion,
        p_codigo_nip, p_ubigueo_pais, p_ubigueo_departamento, p_ubigueo_provincia, p_ubigueo_distrito,
        NOW(), NOW()
    );

    -- Obtener el ID generado
    SET p_id_cliente_out = LAST_INSERT_ID();

    -- 2. Insertar en tabla actividad
    INSERT INTO ficha_cliente_actividad (
        id_cliente, tipo_compra, tipo_comunicacion, nombre_patrocinador,
        nombre_lider_ascendente, interes_principal,descuento_promocion,id_nivel_promocion, created_at, updated_at
    ) VALUES (
        p_id_cliente_out, p_tipo_compra, p_tipo_comunicacion, p_nombre_patrocinador,
        p_nombre_lider_ascendente, p_interes_principal,p_descuento_promocion,p_id_nivel_promocion, NOW(), NOW()
    );
    -- 3. Insertar en tabla seguimiento SOLO SI hay datos
    -- Solo inserta si al menos la observación tiene contenido o hay alguna fecha
    IF (p_observacion_seguimiento IS NOT NULL AND TRIM(p_observacion_seguimiento) != '') 
       OR p_fecha_llamada IS NOT NULL 
       OR p_fecha_proxima IS NOT NULL THEN
        INSERT INTO ficha_cliente_seguimiento (
            id_cliente, fecha_llamada, fecha_proxima, observacion, created_at, updated_at
        ) VALUES (
            p_id_cliente_out, p_fecha_llamada, p_fecha_proxima, p_observacion_seguimiento, NOW(), NOW()
        );
    END IF;

    COMMIT;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_ELIMINAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_ELIMINAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_ELIMINAR`(IN p_id_cliente INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Actualizar el estado is_deleted = 'S' en lugar de eliminar
    UPDATE administracion_cliente
    SET 
        is_deleted = 'S',
        updated_at = NOW()
    WHERE id_cliente = p_id_cliente;

    COMMIT;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_LISTAR`()
BEGIN
			 
	  SELECT
				 ROW_NUMBER() OVER (ORDER BY AE.id_cliente) AS RowIndex
					,AE.id_cliente
					-- ,CONCAT(AE.nombre,' ',AE.apellido) AS nombre
				 , AE.nombre
				 , AE.apellido
				 , AE.telefono
				 , AE.email
				 , AE.direccion
				 , AE.ruc
				 , AE.razon_social
				 , AE.created_at
				 , AE.Activo
 
					
		FROM  administracion_cliente AE ORDER BY AE.id_cliente DESC;
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_NIVEL_PROMOCION_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_NIVEL_PROMOCION_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_NIVEL_PROMOCION_LISTAR`(IN p_tipo_cliente INT)
BEGIN
    SELECT 
         N.id_nivel_promocion,
         N.nombre,
         D.tipo_cliente
    FROM ficha_cliente_actividad_nivel_promocion N
    INNER JOIN ficha_cliente_actividad_nivel_promocion_detalle D 
            ON N.id_nivel_promocion = D.id_nivel_promocion
    WHERE  D.tipo_cliente = p_tipo_cliente  -- Usuario normal ve solo su tipo
    GROUP BY 
         N.id_nivel_promocion, N.nombre, D.tipo_cliente   -- por si un nivel tiene varios tipos
    ORDER BY 
         N.nombre ASC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_OBTENER`(IN p_id_cliente INT)
BEGIN
    SELECT
        -- Datos del cliente
        c.id_cliente,
        c.id_usuario,
        c.nombre,
        c.apellido,
        c.telefono,
        c.email,
        c.direccion,
        c.ruc,
        c.razon_social,
        c.fecha_nacimiento,
        c.Activo,
        c.observacion,
        -- Nuevos
        c.codigo_nip,
        c.ubigueo_pais,
        c.ubigueo_departamento,
        c.ubigueo_provincia,
        c.ubigueo_distrito,
        c.created_at,
        c.updated_at,
        -- Datos de actividad
        a.id_ficha_actividad,
        a.tipo_compra,
        a.tipo_comunicacion,
        a.nombre_patrocinador,
        a.nombre_lider_ascendente,
        a.interes_principal,
				
				a.descuento_promocion,
				a.id_nivel_promocion,
				
        -- Datos de seguimiento
        s.id_ficha_seguimiento,
        s.fecha_llamada,
        s.fecha_proxima,
        s.observacion AS observacion_seguimiento
    FROM administracion_cliente c
    LEFT JOIN ficha_cliente_actividad a ON c.id_cliente = a.id_cliente
    LEFT JOIN ficha_cliente_seguimiento s ON c.id_cliente = s.id_cliente
    WHERE c.id_cliente = p_id_cliente;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ACTUALIZAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ACTUALIZAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ACTUALIZAR`(IN p_id_ficha_seguimiento INT,
    IN p_fecha_llamada TIMESTAMP,
    IN p_fecha_proxima TIMESTAMP,
    IN p_observacion TEXT)
BEGIN
    UPDATE ficha_cliente_seguimiento
    SET
        fecha_llamada = p_fecha_llamada,
        fecha_proxima = p_fecha_proxima,
        observacion = p_observacion,
        updated_at = NOW()
    WHERE id_ficha_seguimiento = p_id_ficha_seguimiento;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_CREAR`(IN p_id_cliente INT,
    IN p_fecha_llamada TIMESTAMP,
    IN p_fecha_proxima TIMESTAMP,
    IN p_observacion TEXT,
    OUT p_id_seguimiento_out INT)
BEGIN
    INSERT INTO ficha_cliente_seguimiento (
        id_cliente, fecha_llamada, fecha_proxima, observacion, created_at, updated_at
    ) VALUES (
        p_id_cliente, p_fecha_llamada, p_fecha_proxima, p_observacion, NOW(), NOW()
    );
    
    SET p_id_seguimiento_out = LAST_INSERT_ID();
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ELIMINAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ELIMINAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_ELIMINAR`(IN p_id_ficha_seguimiento INT)
BEGIN
    DELETE FROM ficha_cliente_seguimiento
    WHERE id_ficha_seguimiento = p_id_ficha_seguimiento;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_CLIENTE_SEGUIMIENTO_LISTAR`(IN p_id_cliente INT)
BEGIN
    SELECT
        id_ficha_seguimiento,
        id_cliente,
        fecha_llamada,
        fecha_proxima,
        observacion,
        created_at,
        updated_at
    FROM ficha_cliente_seguimiento
    WHERE id_cliente = p_id_cliente
    ORDER BY created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_EMPLEADO_ACTUALIZAR_VALIDAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_EMPLEADO_ACTUALIZAR_VALIDAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_EMPLEADO_ACTUALIZAR_VALIDAR`(IN dni_ 						VARCHAR(255),
IN email_ 					VARCHAR(255),
IN id_empleado_ 		VARCHAR(255))
BEGIN
 
 
 DECLARE MensajeValidacion1	VARCHAR(50) DEFAULT  '';
 DECLARE MensajeValidacion2	VARCHAR(50) DEFAULT  '';
 
 DECLARE isDNI 										VARCHAR(50);
 DECLARE isDNI_TOTAL 							VARCHAR(50);
	
 DECLARE isEMAIL_EMPLEADO 				VARCHAR(50);
 DECLARE isEMAIL_EMPLEADO_TOTAL 	VARCHAR(50);
 

 SET isDNI = (SELECT dni FROM administracion_empleado   WHERE id_empleado = id_empleado_);
 SET isDNI_TOTAL = (SELECT dni FROM administracion_empleado   WHERE dni = dni_);
 
 SET isEMAIL_EMPLEADO = (SELECT email FROM administracion_empleado   WHERE id_empleado = id_empleado_);
 SET isEMAIL_EMPLEADO_TOTAL = (SELECT email FROM administracion_empleado   WHERE email = email_);
	
	# CASO 1: DNI
	IF (isDNI = dni_) THEN
		SET MensajeValidacion1 = '';
	ELSEIF (isDNI = isDNI_TOTAL) THEN
		SET MensajeValidacion1 = '';
	ELSEIF (isDNI_TOTAL IS NULL) THEN
		SET MensajeValidacion1 = '';
	ELSE
	 SET MensajeValidacion1 = 'DNI';

	END IF;
	

		# CASO 2: EMAIL
	IF (isEMAIL_EMPLEADO = email_) THEN
		SET MensajeValidacion2 = '';
	ELSEIF (isEMAIL_EMPLEADO = isEMAIL_EMPLEADO_TOTAL) THEN
		SET MensajeValidacion2 = '';
	ELSEIF (isEMAIL_EMPLEADO_TOTAL IS NULL) THEN
		SET MensajeValidacion2 = '';
	ELSE
		SET MensajeValidacion2 = 'EMAIL';
	END IF;

		# CASO 2: EMAIL
	IF (MensajeValidacion1 = 'DNI' OR MensajeValidacion2 = 'EMAIL') THEN
			SELECT CONCAT('¡El ', MensajeValidacion1,' , ',MensajeValidacion2,' ingresado ya existe!') AS MensajeValidacion;
	ELSE
		 SELECT '' AS MensajeValidacion;
	END IF;
 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_EMPLEADO_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_EMPLEADO_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_EMPLEADO_CREAR`(IN nombre_ 							VARCHAR(255),
IN apellido_ 						VARCHAR(255),
IN telefono_ 						VARCHAR(255),
IN email_ 							VARCHAR(255),
IN direccion_						VARCHAR(255),
IN dni_ 								VARCHAR(255),
IN licencia_ 						VARCHAR(255),
IN Activo_ 							CHAR(1),

IN email_acceso_ 				VARCHAR(255),
IN password_ 	  				VARCHAR(255),
IN id_usuario_ 	  			INT)
BEGIN					


IF email_acceso_!= '' AND password_!='' THEN
		BEGIN

			INSERT INTO `users`
			 (
				 `name`
			 , `email`
			 , `id_owner`
			 ,`password`
			 
			 ,`created_at` 
			 ,`updated_at`
			 ,`Activo`) 
			 VALUES (
				nombre_
			 ,email_acceso_
			 ,id_usuario_
			 ,password_
			 
			 ,NOW()
			 ,NOW()
			 ,Activo_

			 );

		END;
		
END IF;

# 2. Obtenemos un nuevo Id para el usuario asignado si es que lo crea
INSERT INTO `administracion_empleado` 
( `nombre`, `apellido`, `telefono`, `email`, `direccion`, `dni`, `licencia`, `created_at`, `updated_at`, `Activo`,id_usuario) 
VALUES
 (
 nombre_
 ,apellido_
 ,telefono_
 ,email_
 ,direccion_
 ,dni_
 ,licencia_
 ,NOW()
 ,NOW()
 ,Activo_
 ,LAST_INSERT_ID()
 );


						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_EMPLEADO_CREAR_VALIDAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_EMPLEADO_CREAR_VALIDAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_EMPLEADO_CREAR_VALIDAR`(IN dni_ 						VARCHAR(255),
IN email_acceso_ 		VARCHAR(255),
IN email_ 					VARCHAR(255))
BEGIN
 
 
 DECLARE MensajeValidacion VARCHAR(50) DEFAULT  '';
      
 IF EXISTS ( SELECT (1) FROM administracion_empleado   WHERE dni = dni_ ) THEN
    BEGIN      
			SET MensajeValidacion = '¡El DNI ingresado ya existe!';
		END;
 END IF;
 
  IF EXISTS ( SELECT (1) FROM administracion_empleado   WHERE email COLLATE utf8mb4_unicode_ci = email_ ) THEN
    BEGIN      
			SET MensajeValidacion = '¡El EMAIL Empleado, ingresado ya existe!';
		END;
 END IF;
 
  IF EXISTS ( SELECT (1) FROM users   WHERE email  COLLATE utf8mb4_unicode_ci = email_acceso_ ) THEN
    BEGIN      
			SET MensajeValidacion = '¡El EMAIL ingresado ya existe!';
		END;
 END IF;
 
 SELECT MensajeValidacion AS MensajeValidacion;
 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_EMPLEADO_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_EMPLEADO_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_EMPLEADO_LISTAR`()
BEGIN
			 
	  SELECT
				 ROW_NUMBER() OVER (ORDER BY AE.id_empleado) AS RowIndex				
				,AE.id_empleado
				,AE.nombre
				,AE.apellido
				,AE.telefono
				,AE.email
				,AE.direccion
				,AE.dni
				,AE.created_at
				,AE.Activo
				,AE.id_usuario
				,U.name AS nombre_usuario
				,AE.licencia
				,CONCAT(AE.nombre,' , ',AE.apellido) AS nombre_apellido

		FROM  administracion_empleado AE LEFT JOIN users U ON AE.id_usuario = U.id
		
		 ORDER BY AE.id_empleado DESC;
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_PAGINA_ACCESO_LISTAR`()
BEGIN
    SELECT 
        id,
        id_menu,
        nombre,
        ruta,
        icono,
        tipo,
        id_padre,
        orden,
        IF(icono IS NULL OR icono = '', UPPER(SUBSTRING(nombre, 1, 2)), NULL) AS iconText,
        COALESCE(objetos_asignados, '[]') AS objetos_asignados
    FROM sistema_menu
    ORDER BY COALESCE(id_padre, 0), orden;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ADMINISTRACION_PERFIL_LISTAR_LOGIN
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ADMINISTRACION_PERFIL_LISTAR_LOGIN`;
delimiter ;;
CREATE PROCEDURE `USP_ADMINISTRACION_PERFIL_LISTAR_LOGIN`(IN p_id_usuario INT,
    IN p_id_perfil INT,
    IN p_id_roles INT)
BEGIN
    DECLARE v_clausula_where TEXT DEFAULT '';
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error al ejecutar la consulta dinámica';
    END;
    
    -- Validar que p_id_usuario no sea NULL
    IF p_id_usuario IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El parámetro p_id_usuario no puede ser NULL';
    END IF;

    -- Construir cláusula WHERE dinámica para filtros adicionales
    IF p_id_perfil IS NOT NULL AND p_id_roles IS NOT NULL THEN
        SET v_clausula_where = CONCAT(' AND SP.id_perfil = ', p_id_perfil, ' AND SRP.id_roles = ', p_id_roles);
    ELSEIF p_id_perfil IS NOT NULL THEN
        SET v_clausula_where = CONCAT(' AND SP.id_perfil = ', p_id_perfil);
    ELSEIF p_id_roles IS NOT NULL THEN
        SET v_clausula_where = CONCAT(' AND SRP.id_roles = ', p_id_roles);
    END IF;

    -- Construir la consulta SQL dinámica
    SET @sql = CONCAT('
        WITH conteo_perfiles AS (
            SELECT
                SPU.id_usuario,
                COUNT(DISTINCT SPU.id_perfil) AS cantidad_perfiles
            FROM seguridad_perfil_users SPU
            WHERE SPU.id_usuario = ', QUOTE(p_id_usuario), '
            GROUP BY SPU.id_usuario
        ),
        perfil AS (
            SELECT
                SPU.id_usuario,
                U.name AS nombre_usuario,
                U.email,
                SP.nombre AS nombre_perfil,
                SP.id_perfil,
                SRP.id_roles,
                ORS.id_menu,
                ORS.id_objetos 
            FROM users U 
                INNER JOIN seguridad_perfil_users SPU ON U.id = SPU.id_usuario
                INNER JOIN seguridad_perfil SP ON SPU.id_perfil = SP.id_perfil
                INNER JOIN seguridad_roles_perfil SRP ON SPU.id_perfil = SRP.id_perfil
                INNER JOIN sistema_menu_objetos_roles SM ON SRP.id_roles = SM.id_roles
                INNER JOIN sistema_menu_objetos ORS ON SM.id_menu_objetos = ORS.id_menu_objetos
            WHERE
                SPU.id_usuario = ', QUOTE(p_id_usuario), '
                AND ORS.id_objetos IS NOT NULL
                ', v_clausula_where, '
            ORDER BY
                SPU.id_perfil ASC
        ),
        resultado AS (
            SELECT
                id_usuario,
                nombre_usuario,
                email,
                nombre_perfil,
                id_perfil,
                id_roles,
                CONCAT(''|'', id_menu, ''_'', GROUP_CONCAT(DISTINCT id_objetos ORDER BY id_objetos ASC SEPARATOR '',''), ''|'') AS permisos
            FROM perfil
            GROUP BY
                id_usuario,
                nombre_usuario,
                email,
                nombre_perfil,
                id_perfil,
                id_roles,
                id_menu
        )
        SELECT
            r.id_usuario,
            r.nombre_usuario,
            r.email,
            r.nombre_perfil,
            r.id_perfil,
            r.id_roles,
            cp.cantidad_perfiles,
            GROUP_CONCAT(DISTINCT r.permisos ORDER BY r.permisos ASC SEPARATOR ''-'') AS menu_objetos
        FROM resultado r
        INNER JOIN conteo_perfiles cp ON r.id_usuario = cp.id_usuario
        GROUP BY
            r.id_usuario,
            r.nombre_usuario,
            r.email,
            r.nombre_perfil,
            r.id_perfil,
            r.id_roles,
            cp.cantidad_perfiles;
    ');
    
    -- Ejecutar la consulta dinámica
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_CARRITO_AGREGAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_CARRITO_AGREGAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_CARRITO_AGREGAR`(IN p_carrito_id INT,
    IN p_producto_id INT,
    IN p_variante_id INT,
    IN p_cantidad INT,
    IN p_precio_unitario DECIMAL(10,2))
BEGIN
    DECLARE v_item_id INT;
    DECLARE v_cantidad_actual INT;

    -- Verificar si ya existe
    SELECT id, cantidad INTO v_item_id, v_cantidad_actual
    FROM ecommerce_carrito_items
    WHERE carrito_id = p_carrito_id
        AND producto_id = p_producto_id
        AND (variante_id = p_variante_id OR (variante_id IS NULL AND p_variante_id IS NULL))
    LIMIT 1;

    IF v_item_id IS NOT NULL THEN
        UPDATE ecommerce_carrito_items
        SET cantidad = v_cantidad_actual + p_cantidad,
            precio_unitario = p_precio_unitario,
            updated_at = NOW()
        WHERE id = v_item_id;
    ELSE
        INSERT INTO ecommerce_carrito_items (carrito_id, producto_id, variante_id, cantidad, precio_unitario)
        VALUES (p_carrito_id, p_producto_id, p_variante_id, p_cantidad, p_precio_unitario);
    END IF;

    SELECT ROW_COUNT() AS affected_rows;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_CARRITO_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_CARRITO_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_CARRITO_OBTENER`(IN p_cliente_id INT,
    IN p_session_id VARCHAR(100))
BEGIN
    DECLARE v_carrito_id INT;

    -- Buscar o crear carrito
    IF p_cliente_id IS NOT NULL THEN
        SELECT id INTO v_carrito_id FROM ecommerce_carritos WHERE cliente_id = p_cliente_id LIMIT 1;
        IF v_carrito_id IS NULL THEN
            INSERT INTO ecommerce_carritos (cliente_id, session_id) VALUES (p_cliente_id, NULL);
            SET v_carrito_id = LAST_INSERT_ID();
        END IF;
    ELSE
        SELECT id INTO v_carrito_id FROM ecommerce_carritos WHERE session_id = p_session_id LIMIT 1;
        IF v_carrito_id IS NULL THEN
            INSERT INTO ecommerce_carritos (cliente_id, session_id) VALUES (NULL, p_session_id);
            SET v_carrito_id = LAST_INSERT_ID();
        END IF;
    END IF;

    -- Carrito info
    SELECT c.*, cu.codigo AS cupon_codigo, cu.tipo AS cupon_tipo, cu.valor AS cupon_valor
    FROM ecommerce_carritos c
    LEFT JOIN ecommerce_cupones cu ON c.cupon_id = cu.id
    WHERE c.id = v_carrito_id;

    -- Items del carrito
    SELECT
        ci.*,
        p.nombre AS producto_nombre,
        p.slug AS producto_slug,
        p.imagen_principal,
        p.stock AS producto_stock,
        v.nombre AS variante_nombre,
        v.atributos AS variante_atributos,
        v.stock AS variante_stock
    FROM ecommerce_carrito_items ci
    JOIN ecommerce_productos p ON ci.producto_id = p.id
    LEFT JOIN ecommerce_producto_variantes v ON ci.variante_id = v.id
    WHERE ci.carrito_id = v_carrito_id;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_CATEGORIAS_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_CATEGORIAS_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_CATEGORIAS_LISTAR`(IN p_solo_padres TINYINT)
BEGIN
    SELECT
        c.id,
        c.nombre,
        c.slug,
        c.descripcion,
        c.imagen_url,
        c.padre_id,
        c.orden,
        (SELECT COUNT(*) FROM ecommerce_productos p WHERE p.categoria_id = c.id AND p.activo = 1) AS productos_count
    FROM ecommerce_categorias c
    WHERE c.activo = 1
        AND (p_solo_padres = 0 OR c.padre_id IS NULL)
    ORDER BY c.orden ASC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_DASHBOARD_STATS
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_DASHBOARD_STATS`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_DASHBOARD_STATS`()
BEGIN
    -- Total productos
    SELECT COUNT(*) AS total_productos FROM ecommerce_productos WHERE activo = 1;

    -- Total clientes
    SELECT COUNT(*) AS total_clientes FROM ecommerce_clientes WHERE activo = 1;

    -- Pedidos por estado
    SELECT estado, COUNT(*) AS cantidad
    FROM ecommerce_pedidos
    WHERE deleted_at IS NULL
    GROUP BY estado;

    -- Ventas del mes
    SELECT
        SUM(total) AS ventas_mes,
        COUNT(*) AS pedidos_mes
    FROM ecommerce_pedidos
    WHERE pagado = 1
        AND MONTH(created_at) = MONTH(NOW())
        AND YEAR(created_at) = YEAR(NOW());

    -- Productos más vendidos
    SELECT
        p.id, p.nombre, p.imagen_principal,
        SUM(pi.cantidad) AS total_vendido
    FROM ecommerce_pedido_items pi
    JOIN ecommerce_productos p ON pi.producto_id = p.id
    JOIN ecommerce_pedidos pe ON pi.pedido_id = pe.id
    WHERE pe.pagado = 1 AND pe.deleted_at IS NULL
    GROUP BY p.id, p.nombre, p.imagen_principal
    ORDER BY total_vendido DESC
    LIMIT 10;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_DIRECCIONES_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_DIRECCIONES_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_DIRECCIONES_LISTAR`(IN p_cliente_id INT)
BEGIN
    SELECT * FROM ecommerce_direcciones
    WHERE cliente_id = p_cliente_id AND activo = 1
    ORDER BY es_principal DESC, created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_DIRECCION_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_DIRECCION_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_DIRECCION_CREAR`(IN p_cliente_id INT,
    IN p_tipo VARCHAR(20),
    IN p_nombre_completo VARCHAR(150),
    IN p_telefono VARCHAR(20),
    IN p_direccion VARCHAR(255),
    IN p_direccion_adicional VARCHAR(255),
    IN p_ciudad VARCHAR(100),
    IN p_estado VARCHAR(100),
    IN p_codigo_postal VARCHAR(20),
    IN p_pais VARCHAR(100),
    IN p_latitud DECIMAL(10,8),
    IN p_longitud DECIMAL(11,8),
    IN p_es_principal TINYINT,
    OUT p_direccion_id INT)
BEGIN
    -- Si es principal, quitar principal de otras
    IF p_es_principal = 1 THEN
        UPDATE ecommerce_direcciones SET es_principal = 0 WHERE cliente_id = p_cliente_id;
    END IF;

    -- Si es la primera, hacerla principal
    IF NOT EXISTS (SELECT 1 FROM ecommerce_direcciones WHERE cliente_id = p_cliente_id) THEN
        SET p_es_principal = 1;
    END IF;

    INSERT INTO ecommerce_direcciones (
        cliente_id, tipo, nombre_completo, telefono, direccion, direccion_adicional,
        ciudad, estado, codigo_postal, country, latitud, longitud, es_principal
    ) VALUES (
        p_cliente_id, p_tipo, p_nombre_completo, p_telefono, p_direccion, p_direccion_adicional,
        p_ciudad, p_estado, p_codigo_postal, p_pais, p_latitud, p_longitud, p_es_principal
    );

    SET p_direccion_id = LAST_INSERT_ID();
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PEDIDOS_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PEDIDOS_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PEDIDOS_LISTAR`(IN p_cliente_id INT,
    IN p_estado VARCHAR(20),
    IN p_pagina INT,
    IN p_por_pagina INT)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pagina - 1) * p_por_pagina;

    SELECT
        p.*,
        me.nombre AS metodo_envio_nombre,
        mp.nombre AS metodo_pago_nombre,
        (SELECT COUNT(*) FROM ecommerce_pedido_items pi WHERE pi.pedido_id = p.id) AS items_count
    FROM ecommerce_pedidos p
    LEFT JOIN ecommerce_metodos_envio me ON p.metodo_envio_id = me.id
    LEFT JOIN ecommerce_metodos_pago mp ON p.metodo_pago_id = mp.id
    WHERE p.cliente_id = p_cliente_id
        AND p.deleted_at IS NULL
        AND (p_estado IS NULL OR p.estado = p_estado)
    ORDER BY p.created_at DESC
    LIMIT p_por_pagina OFFSET v_offset;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PEDIDO_CAMBIARESTADO
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PEDIDO_CAMBIARESTADO`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PEDIDO_CAMBIARESTADO`(IN p_pedido_id INT,
    IN p_nuevo_estado VARCHAR(20),
    IN p_comentario TEXT,
    IN p_creado_por VARCHAR(100))
BEGIN
    DECLARE v_estado_anterior VARCHAR(20);

    SELECT estado INTO v_estado_anterior FROM ecommerce_pedidos WHERE id = p_pedido_id;

    UPDATE ecommerce_pedidos
    SET estado = p_nuevo_estado, updated_at = NOW()
    WHERE id = p_pedido_id;

    INSERT INTO ecommerce_pedido_historial (pedido_id, estado_anterior, estado_nuevo, comentario, creado_por)
    VALUES (p_pedido_id, v_estado_anterior, p_nuevo_estado, p_comentario, p_creado_por);

    SELECT ROW_COUNT() AS affected_rows;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PEDIDO_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PEDIDO_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PEDIDO_CREAR`(IN p_cliente_id INT,
    IN p_direccion_envio JSON,
    IN p_metodo_envio_id INT,
    IN p_metodo_pago_id INT,
    IN p_subtotal DECIMAL(10,2),
    IN p_descuento DECIMAL(10,2),
    IN p_cupon_id INT,
    IN p_envio DECIMAL(10,2),
    IN p_impuestos DECIMAL(10,2),
    IN p_total DECIMAL(10,2),
    IN p_notas TEXT,
    IN p_ip_address VARCHAR(45),
    OUT p_pedido_id INT,
    OUT p_numero_pedido VARCHAR(30))
BEGIN
    DECLARE v_year VARCHAR(4);
    DECLARE v_ultimo_numero INT;

    SET v_year = YEAR(NOW());

    -- Generar número de pedido
    SELECT COALESCE(MAX(CAST(SUBSTRING(numero_pedido, -5) AS UNSIGNED)), 0) + 1
    INTO v_ultimo_numero
    FROM ecommerce_pedidos
    WHERE YEAR(created_at) = v_year;

    SET p_numero_pedido = CONCAT('ORD-', v_year, '-', LPAD(v_ultimo_numero, 5, '0'));

    -- Crear pedido
    INSERT INTO ecommerce_pedidos (
        cliente_id, numero_pedido, estado, direccion_envio, metodo_envio_id,
        metodo_pago_id, subtotal, descuento, cupon_id, envio, impuestos,
        total, notas, pagado, ip_address, created_at, updated_at
    ) VALUES (
        p_cliente_id, p_numero_pedido, 'pendiente', p_direccion_envio, p_metodo_envio_id,
        p_metodo_pago_id, p_subtotal, p_descuento, p_cupon_id, p_envio, p_impuestos,
        p_total, p_notas, 0, p_ip_address, NOW(), NOW()
    );

    SET p_pedido_id = LAST_INSERT_ID();

    -- Registrar historial
    INSERT INTO ecommerce_pedido_historial (pedido_id, estado_anterior, estado_nuevo, comentario, creado_por)
    VALUES (p_pedido_id, NULL, 'pendiente', 'Pedido creado', 'sistema');
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PEDIDO_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PEDIDO_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PEDIDO_OBTENER`(IN p_pedido_id INT,
    IN p_cliente_id INT)
BEGIN
    -- Pedido
    SELECT
        p.*,
        me.nombre AS metodo_envio_nombre,
        mp.nombre AS metodo_pago_nombre,
        cu.codigo AS cupon_codigo
    FROM ecommerce_pedidos p
    LEFT JOIN ecommerce_metodos_envio me ON p.metodo_envio_id = me.id
    LEFT JOIN ecommerce_metodos_pago mp ON p.metodo_pago_id = mp.id
    LEFT JOIN ecommerce_cupones cu ON p.cupon_id = cu.id
    WHERE p.id = p_pedido_id AND p.cliente_id = p_cliente_id AND p.deleted_at IS NULL;

    -- Items
    SELECT pi.*, pr.imagen_principal
    FROM ecommerce_pedido_items pi
    LEFT JOIN ecommerce_productos pr ON pi.producto_id = pr.id
    WHERE pi.pedido_id = p_pedido_id;

    -- Historial
    SELECT * FROM ecommerce_pedido_historial
    WHERE pedido_id = p_pedido_id
    ORDER BY created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PRODUCTOS_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PRODUCTOS_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PRODUCTOS_LISTAR`(IN p_categoria_id INT,
    IN p_marca_id INT,
    IN p_precio_min DECIMAL(10,2),
    IN p_precio_max DECIMAL(10,2),
    IN p_destacados TINYINT,
    IN p_nuevos TINYINT,
    IN p_en_oferta TINYINT,
    IN p_busqueda VARCHAR(255),
    IN p_ordenar VARCHAR(50),
    IN p_direccion VARCHAR(4),
    IN p_pagina INT,
    IN p_por_pagina INT)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pagina - 1) * p_por_pagina;

    SELECT
        p.id,
        p.categoria_id,
        p.marca_id,
        p.nombre,
        p.slug,
        p.descripcion_corta,
        p.precio,
        p.precio_oferta,
        COALESCE(p.precio_oferta, p.precio) AS precio_final,
        p.imagen_principal,
        p.stock,
        p.destacado,
        p.nuevo,
        c.nombre AS categoria_nombre,
        m.nombre AS marca_nombre,
        (SELECT AVG(rating) FROM ecommerce_reviews r WHERE r.producto_id = p.id AND r.aprobado = 1) AS rating_promedio,
        (SELECT COUNT(*) FROM ecommerce_reviews r WHERE r.producto_id = p.id AND r.aprobado = 1) AS reviews_count
    FROM ecommerce_productos p
    LEFT JOIN ecommerce_categorias c ON p.categoria_id = c.id
    LEFT JOIN ecommerce_marcas m ON p.marca_id = m.id
    WHERE p.activo = 1
        AND (p_categoria_id IS NULL OR p.categoria_id = p_categoria_id)
        AND (p_marca_id IS NULL OR p.marca_id = p_marca_id)
        AND (p_precio_min IS NULL OR p.precio >= p_precio_min)
        AND (p_precio_max IS NULL OR p.precio <= p_precio_max)
        AND (p_destacados = 0 OR p.destacado = 1)
        AND (p_nuevos = 0 OR p.nuevo = 1)
        AND (p_en_oferta = 0 OR p.precio_oferta IS NOT NULL)
        AND (p_busqueda IS NULL OR p.nombre LIKE CONCAT('%', p_busqueda, '%') OR p.descripcion LIKE CONCAT('%', p_busqueda, '%'))
    ORDER BY
        CASE WHEN p_ordenar = 'precio' AND p_direccion = 'asc' THEN COALESCE(p.precio_oferta, p.precio) END ASC,
        CASE WHEN p_ordenar = 'precio' AND p_direccion = 'desc' THEN COALESCE(p.precio_oferta, p.precio) END DESC,
        CASE WHEN p_ordenar = 'nombre' AND p_direccion = 'asc' THEN p.nombre END ASC,
        CASE WHEN p_ordenar = 'nombre' AND p_direccion = 'desc' THEN p.nombre END DESC,
        CASE WHEN p_ordenar = 'created_at' OR p_ordenar IS NULL THEN p.created_at END DESC
    LIMIT p_por_pagina OFFSET v_offset;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_PRODUCTO_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_PRODUCTO_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_PRODUCTO_OBTENER`(IN p_id INT)
BEGIN
    SELECT
        p.*,
        c.nombre AS categoria_nombre,
        c.slug AS categoria_slug,
        m.nombre AS marca_nombre,
        m.logo_url AS marca_logo,
        (SELECT AVG(rating) FROM ecommerce_reviews r WHERE r.producto_id = p.id AND r.aprobado = 1) AS rating_promedio,
        (SELECT COUNT(*) FROM ecommerce_reviews r WHERE r.producto_id = p.id AND r.aprobado = 1) AS reviews_count
    FROM ecommerce_productos p
    LEFT JOIN ecommerce_categorias c ON p.categoria_id = c.id
    LEFT JOIN ecommerce_marcas m ON p.marca_id = m.id
    WHERE p.id = p_id AND p.activo = 1;

    -- Variantes
    SELECT * FROM ecommerce_producto_variantes
    WHERE producto_id = p_id AND activo = 1;

    -- Reviews recientes
    SELECT r.*, cl.nombre, cl.apellido, cl.avatar
    FROM ecommerce_reviews r
    JOIN ecommerce_clientes cl ON r.cliente_id = cl.id
    WHERE r.producto_id = p_id AND r.aprobado = 1
    ORDER BY r.created_at DESC
    LIMIT 5;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_REPORTE_VENTAS
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_REPORTE_VENTAS`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_REPORTE_VENTAS`(IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE)
BEGIN
    SELECT
        DATE(created_at) AS fecha,
        COUNT(*) AS total_pedidos,
        SUM(subtotal) AS subtotal,
        SUM(descuento) AS descuento,
        SUM(envio) AS envio,
        SUM(impuestos) AS impuestos,
        SUM(total) AS total
    FROM ecommerce_pedidos
    WHERE pagado = 1
        AND deleted_at IS NULL
        AND DATE(created_at) BETWEEN p_fecha_inicio AND p_fecha_fin
    GROUP BY DATE(created_at)
    ORDER BY fecha;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_WISHLIST_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_WISHLIST_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_WISHLIST_LISTAR`(IN p_cliente_id INT)
BEGIN
    SELECT
        w.id,
        w.producto_id,
        w.created_at AS fecha_agregado,
        p.nombre,
        p.slug,
        p.descripcion_corta,
        p.precio,
        p.precio_oferta,
        COALESCE(p.precio_oferta, p.precio) AS precio_final,
        p.imagen_principal,
        p.stock,
        c.nombre AS categoria_nombre,
        m.nombre AS marca_nombre
    FROM ecommerce_wishlist w
    JOIN ecommerce_productos p ON w.producto_id = p.id
    LEFT JOIN ecommerce_categorias c ON p.categoria_id = c.id
    LEFT JOIN ecommerce_marcas m ON p.marca_id = m.id
    WHERE w.cliente_id = p_cliente_id AND p.activo = 1
    ORDER BY w.created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_ECOMMERCE_WISHLIST_TOGGLE
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_ECOMMERCE_WISHLIST_TOGGLE`;
delimiter ;;
CREATE PROCEDURE `USP_ECOMMERCE_WISHLIST_TOGGLE`(IN p_cliente_id INT,
    IN p_producto_id INT,
    OUT p_in_wishlist TINYINT)
BEGIN
    DECLARE v_existe INT;

    SELECT id INTO v_existe
    FROM ecommerce_wishlist
    WHERE cliente_id = p_cliente_id AND producto_id = p_producto_id
    LIMIT 1;

    IF v_existe IS NOT NULL THEN
        DELETE FROM ecommerce_wishlist WHERE id = v_existe;
        SET p_in_wishlist = 0;
    ELSE
        INSERT INTO ecommerce_wishlist (cliente_id, producto_id) VALUES (p_cliente_id, p_producto_id);
        SET p_in_wishlist = 1;
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_ACTUALIZAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_ACTUALIZAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_ACTUALIZAR`(IN p_id_cotizacion INT,
    IN p_codigo VARCHAR(50),
    IN p_version VARCHAR(10),
    IN p_proyecto VARCHAR(500),
    IN p_etapa VARCHAR(200),
    IN p_cliente VARCHAR(255),
    IN p_ruc VARCHAR(20),
    IN p_fecha DATE,
    IN p_direccion VARCHAR(500),
    IN p_ciudad VARCHAR(100),
    IN p_provincia VARCHAR(100),
    IN p_validez_oferta VARCHAR(200),
    IN p_tiempo_ejecucion VARCHAR(200),
    IN p_forma_pago VARCHAR(500),
    IN p_adelanto VARCHAR(200),
    IN p_garantia VARCHAR(500),
    IN p_observaciones TEXT,
    IN p_costo_directo DECIMAL(12,2),
    IN p_gastos_generales DECIMAL(5,2),
    IN p_utilidad DECIMAL(5,2),
    IN p_subtotal DECIMAL(12,2),
    IN p_igv DECIMAL(5,2),
    IN p_total DECIMAL(12,2))
BEGIN
    UPDATE finanzas_cotizacion SET
        codigo = p_codigo,
        version = p_version,
        proyecto = p_proyecto,
        etapa = p_etapa,
        cliente = p_cliente,
        ruc = p_ruc,
        fecha = p_fecha,
        direccion = p_direccion,
        ciudad = p_ciudad,
        provincia = p_provincia,
        validez_oferta = p_validez_oferta,
        tiempo_ejecucion = p_tiempo_ejecucion,
        forma_pago = p_forma_pago,
        adelanto = p_adelanto,
        garantia = p_garantia,
        observaciones = p_observaciones,
        costo_directo = p_costo_directo,
        gastos_generales = p_gastos_generales,
        utilidad = p_utilidad,
        subtotal = p_subtotal,
        igv = p_igv,
        total = p_total,
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion
    AND Activo = 'S';
    
    SELECT ROW_COUNT() AS rows_affected;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO`(IN p_id_cotizacion INT,
    IN p_estado_nuevo VARCHAR(50),
    IN p_id_usuario INT,
    IN p_comentario TEXT)
BEGIN
    DECLARE v_estado_anterior VARCHAR(50);
    
    -- Obtener estado actual
    SELECT estado INTO v_estado_anterior
    FROM finanzas_cotizacion
    WHERE id_cotizacion = p_id_cotizacion;
    
    -- Actualizar estado
    UPDATE finanzas_cotizacion 
    SET estado = p_estado_nuevo,
        id_usuario_aprobacion = IF(p_estado_nuevo = 'APROBADO', p_id_usuario, id_usuario_aprobacion),
        fecha_aprobacion = IF(p_estado_nuevo = 'APROBADO', NOW(), fecha_aprobacion),
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion;
    
    -- Registrar en historial
    INSERT INTO finanzas_cotizacion_historial (
        id_cotizacion, estado_anterior, estado_nuevo, comentario, id_usuario
    ) VALUES (
        p_id_cotizacion, v_estado_anterior, p_estado_nuevo, p_comentario, p_id_usuario
    );
    
    SELECT ROW_COUNT() AS rows_affected;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO_CON_EVIDENCIA
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO_CON_EVIDENCIA`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_CAMBIAR_ESTADO_CON_EVIDENCIA`(IN p_id_cotizacion INT,
    IN p_estado_nuevo VARCHAR(50),
    IN p_id_usuario INT,
    IN p_comentario TEXT,
    IN p_tiene_evidencia BOOLEAN)
BEGIN
    DECLARE v_estado_anterior VARCHAR(50);
    DECLARE v_id_historial INT;
    
    -- Verificar si el cambio de estado requiere evidencia
    -- Estados que requieren evidencia: APROBADO, RECHAZADO, FACTURADO
    IF p_estado_nuevo IN ('APROBADO', 'RECHAZADO', 'FACTURADO') AND p_tiene_evidencia = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Este cambio de estado requiere adjuntar una evidencia';
    END IF;
    
    -- Obtener estado actual
    SELECT estado INTO v_estado_anterior
    FROM finanzas_cotizacion
    WHERE id_cotizacion = p_id_cotizacion;
    
    -- Actualizar estado
    UPDATE finanzas_cotizacion 
    SET estado = p_estado_nuevo,
        id_usuario_aprobacion = IF(p_estado_nuevo = 'APROBADO', p_id_usuario, id_usuario_aprobacion),
        fecha_aprobacion = IF(p_estado_nuevo = 'APROBADO', NOW(), fecha_aprobacion),
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion;
    
    -- Registrar en historial
    INSERT INTO finanzas_cotizacion_historial (
        id_cotizacion, estado_anterior, estado_nuevo, comentario, id_usuario
    ) VALUES (
        p_id_cotizacion, v_estado_anterior, p_estado_nuevo, p_comentario, p_id_usuario
    );
    
    SET v_id_historial = LAST_INSERT_ID();
    
    SELECT 
        v_id_historial AS id_historial,
        ROW_COUNT() AS rows_affected;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_CREAR`(IN p_codigo VARCHAR(50),
    IN p_version VARCHAR(10),
    IN p_proyecto VARCHAR(500),
    IN p_etapa VARCHAR(200),
    IN p_cliente VARCHAR(255),
    IN p_ruc VARCHAR(20),
    IN p_fecha DATE,
    IN p_direccion VARCHAR(500),
    IN p_ciudad VARCHAR(100),
    IN p_provincia VARCHAR(100),
    IN p_validez_oferta VARCHAR(200),
    IN p_tiempo_ejecucion VARCHAR(200),
    IN p_forma_pago VARCHAR(500),
    IN p_adelanto VARCHAR(200),
    IN p_garantia VARCHAR(500),
    IN p_observaciones TEXT,
    IN p_costo_directo DECIMAL(12,2),
    IN p_gastos_generales DECIMAL(5,2),
    IN p_utilidad DECIMAL(5,2),
    IN p_subtotal DECIMAL(12,2),
    IN p_igv DECIMAL(5,2),
    IN p_total DECIMAL(12,2),
    IN p_id_usuario_creacion INT,
    OUT p_id_cotizacion_out INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_id_cotizacion_out = -1;
    END;
    
    START TRANSACTION;
    
    INSERT INTO finanzas_cotizacion (
        codigo, version, proyecto, etapa, cliente, ruc,
        fecha, direccion, ciudad, provincia,
        validez_oferta, tiempo_ejecucion, forma_pago, adelanto, garantia, observaciones,
        costo_directo, gastos_generales, utilidad, subtotal, igv, total,
        id_usuario_creacion, estado, Activo
    ) VALUES (
        p_codigo, p_version, p_proyecto, p_etapa, p_cliente, p_ruc,
        p_fecha, p_direccion, p_ciudad, p_provincia,
        p_validez_oferta, p_tiempo_ejecucion, p_forma_pago, p_adelanto, p_garantia, p_observaciones,
        p_costo_directo, p_gastos_generales, p_utilidad, p_subtotal, p_igv, p_total,
        p_id_usuario_creacion, 'PENDIENTE', 'S'
    );
    
    SET p_id_cotizacion_out = LAST_INSERT_ID();
    
    -- Insertar en historial
    INSERT INTO finanzas_cotizacion_historial (
        id_cotizacion, estado_anterior, estado_nuevo, comentario, id_usuario
    ) VALUES (
        p_id_cotizacion_out, NULL, 'PENDIENTE', 'Cotización creada', p_id_usuario_creacion
    );
    
    COMMIT;
    
    SELECT p_id_cotizacion_out AS id_cotizacion;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_CREAR_COMPLETO
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_CREAR_COMPLETO`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_CREAR_COMPLETO`(IN p_codigo VARCHAR(50),
    IN p_version VARCHAR(10),
    IN p_proyecto VARCHAR(500),
    IN p_etapa VARCHAR(200),
    IN p_cliente VARCHAR(255),
    IN p_ruc VARCHAR(20),
    IN p_fecha DATE,
    IN p_direccion VARCHAR(500),
    IN p_ciudad VARCHAR(100),
    IN p_provincia VARCHAR(100),
    IN p_validez_oferta VARCHAR(200),
    IN p_tiempo_ejecucion VARCHAR(200),
    IN p_forma_pago VARCHAR(500),
    IN p_adelanto VARCHAR(200),
    IN p_garantia VARCHAR(500),
    IN p_observaciones TEXT,
    IN p_costo_directo DECIMAL(12,2),
    IN p_gastos_generales DECIMAL(5,2),
    IN p_utilidad DECIMAL(5,2),
    IN p_subtotal DECIMAL(12,2),
    IN p_igv DECIMAL(5,2),
    IN p_total DECIMAL(12,2),
    IN p_id_usuario_creacion INT)
BEGIN
    DECLARE v_id_cotizacion INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        -- Devolver error
        SELECT -1 AS id_cotizacion, 'Error al crear cotización' AS mensaje;
    END;
    
    START TRANSACTION;
    
    -- Crear cotización
    INSERT INTO finanzas_cotizacion (
        codigo, version, proyecto, etapa, cliente, ruc,
        fecha, direccion, ciudad, provincia,
        validez_oferta, tiempo_ejecucion, forma_pago, adelanto, garantia, observaciones,
        costo_directo, gastos_generales, utilidad, subtotal, igv, total,
        id_usuario_creacion, estado, Activo
    ) VALUES (
        p_codigo, p_version, p_proyecto, p_etapa, p_cliente, p_ruc,
        p_fecha, p_direccion, p_ciudad, p_provincia,
        p_validez_oferta, p_tiempo_ejecucion, p_forma_pago, p_adelanto, p_garantia, p_observaciones,
        p_costo_directo, p_gastos_generales, p_utilidad, p_subtotal, p_igv, p_total,
        p_id_usuario_creacion, 'PENDIENTE', 'S'
    );
    
    SET v_id_cotizacion = LAST_INSERT_ID();
    
    -- Insertar en historial
    INSERT INTO finanzas_cotizacion_historial (
        id_cotizacion, estado_anterior, estado_nuevo, comentario, id_usuario
    ) VALUES (
        v_id_cotizacion, NULL, 'PENDIENTE', 'Cotización creada', p_id_usuario_creacion
    );
    
    COMMIT;
    
    -- Devolver el ID generado
    SELECT v_id_cotizacion AS id_cotizacion, 'Cotización creada exitosamente' AS mensaje;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_ELIMINAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_ELIMINAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_ELIMINAR`(IN p_id_cotizacion INT)
BEGIN
    -- Eliminación lógica
    UPDATE finanzas_cotizacion 
    SET Activo = 'N',
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion;
    
    -- También desactivar los items
    UPDATE finanzas_cotizacion_items
    SET Activo = 'N',
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion;
    
    SELECT ROW_COUNT() AS rows_affected;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_EVIDENCIAS_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_CREAR`(IN p_id_cotizacion INT,
    IN p_id_historial INT,
    IN p_estado_aplicado VARCHAR(50),
    IN p_nombre_archivo VARCHAR(255),
    IN p_ruta_archivo VARCHAR(500),
    IN p_tipo_archivo VARCHAR(50),
    IN p_tamano_archivo INT,
    IN p_id_usuario_subida INT,
    IN p_comentario TEXT)
BEGIN
    INSERT INTO finanzas_cotizacion_evidencias (
        id_cotizacion,
        id_historial,
        estado_aplicado,
        nombre_archivo,
        ruta_archivo,
        tipo_archivo,
        tamano_archivo,
        id_usuario_subida,
        comentario,
        Activo
    ) VALUES (
        p_id_cotizacion,
        p_id_historial,
        p_estado_aplicado,
        p_nombre_archivo,
        p_ruta_archivo,
        p_tipo_archivo,
        p_tamano_archivo,
        p_id_usuario_subida,
        p_comentario,
        'S'
    );
    
    SELECT LAST_INSERT_ID() AS id_evidencia;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_EVIDENCIAS_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_LISTAR`(IN p_id_cotizacion INT)
BEGIN
    SELECT
        e.id_evidencia,
        e.id_cotizacion,
        e.id_historial,
        e.estado_aplicado,
        e.nombre_archivo,
        e.ruta_archivo,
        e.tipo_archivo,
        e.tamano_archivo,
        e.id_usuario_subida,
        e.comentario,
        e.created_at,
        e.Activo,
        u.name AS usuario_nombre
    FROM finanzas_cotizacion_evidencias e
    LEFT JOIN users u ON e.id_usuario_subida = u.id
    WHERE e.id_cotizacion = p_id_cotizacion
    AND e.Activo = 'S'
    ORDER BY e.created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_EVIDENCIAS_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_OBTENER`(IN p_id_evidencia INT)
BEGIN
    SELECT
        e.id_evidencia,
        e.id_cotizacion,
        e.id_historial,
        e.estado_aplicado,
        e.nombre_archivo,
        e.ruta_archivo,
        e.tipo_archivo,
        e.tamano_archivo,
        e.id_usuario_subida,
        e.comentario,
        e.created_at,
        e.Activo
    FROM finanzas_cotizacion_evidencias e
    WHERE e.id_evidencia = p_id_evidencia
    AND e.Activo = 'S';
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_EVIDENCIAS_POR_ESTADO
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_POR_ESTADO`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_EVIDENCIAS_POR_ESTADO`(IN p_id_cotizacion INT,
    IN p_estado VARCHAR(50))
BEGIN
    SELECT
        e.id_evidencia,
        e.id_cotizacion,
        e.id_historial,
        e.estado_aplicado,
        e.nombre_archivo,
        e.ruta_archivo,
        e.tipo_archivo,
        e.tamano_archivo,
        e.comentario,
        e.created_at
    FROM finanzas_cotizacion_evidencias e
    WHERE e.id_cotizacion = p_id_cotizacion
    AND e.estado_aplicado = p_estado
    AND e.Activo = 'S'
    ORDER BY e.created_at DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_ITEMS_CREAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_ITEMS_CREAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_ITEMS_CREAR`(IN p_id_cotizacion INT,
    IN p_item VARCHAR(20),
    IN p_descripcion VARCHAR(1000),
    IN p_unidad VARCHAR(20),
    IN p_cantidad DECIMAL(10,2),
    IN p_precio_unitario DECIMAL(12,2),
    IN p_subtotal DECIMAL(12,2),
    IN p_orden INT,
    IN p_seccion VARCHAR(200))
BEGIN
    INSERT INTO finanzas_cotizacion_items (
        id_cotizacion, item, descripcion, unidad, cantidad,
        precio_unitario, subtotal, orden, seccion, Activo
    ) VALUES (
        p_id_cotizacion, p_item, p_descripcion, p_unidad, p_cantidad,
        p_precio_unitario, p_subtotal, p_orden, p_seccion, 'S'
    );
    
    SELECT LAST_INSERT_ID() AS id_cotizacion_item;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_ITEMS_ELIMINAR_POR_COTIZACION
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_ITEMS_ELIMINAR_POR_COTIZACION`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_ITEMS_ELIMINAR_POR_COTIZACION`(IN p_id_cotizacion INT)
BEGIN
    UPDATE finanzas_cotizacion_items
    SET Activo = 'N',
        updated_at = NOW()
    WHERE id_cotizacion = p_id_cotizacion;
    
    SELECT ROW_COUNT() AS rows_affected;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_LISTAR`()
BEGIN
    SELECT
        ROW_NUMBER() OVER (ORDER BY c.id_cotizacion DESC) AS RowIndex,
        c.id_cotizacion,
        c.codigo,
        c.version,
        c.proyecto,
        c.etapa,
        c.cliente,
        c.ruc,
        c.fecha,
        c.direccion,
        c.ciudad,
        c.provincia,
        c.validez_oferta,
        c.tiempo_ejecucion,
        c.forma_pago,
        c.adelanto,
        c.garantia,
        c.observaciones,
        c.costo_directo,
        c.gastos_generales,
        c.utilidad,
        c.subtotal,
        c.igv,
        c.total,
        c.estado,
        c.id_usuario_creacion,
        c.id_usuario_aprobacion,
        c.fecha_aprobacion,
        c.created_at,
        c.updated_at,
        c.Activo,
        -- Contar items de la cotización
        (SELECT COUNT(*) FROM finanzas_cotizacion_items ci 
         WHERE ci.id_cotizacion = c.id_cotizacion AND ci.Activo = 'S') AS total_items
    FROM finanzas_cotizacion c
    WHERE c.Activo = 'S'
    ORDER BY c.id_cotizacion DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_FINANZAS_COTIZACIONES_OBTENER
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_FINANZAS_COTIZACIONES_OBTENER`;
delimiter ;;
CREATE PROCEDURE `USP_FINANZAS_COTIZACIONES_OBTENER`(IN p_id_cotizacion INT)
BEGIN
    -- Datos principales de la cotización
    SELECT
        c.id_cotizacion,
        c.codigo,
        c.version,
        c.proyecto,
        c.etapa,
        c.cliente,
        c.ruc,
        c.fecha,
        c.direccion,
        c.ciudad,
        c.provincia,
        c.validez_oferta,
        c.tiempo_ejecucion,
        c.forma_pago,
        c.adelanto,
        c.garantia,
        c.observaciones,
        c.costo_directo,
        c.gastos_generales,
        c.utilidad,
        c.subtotal,
        c.igv,
        c.total,
        c.estado,
        c.id_usuario_creacion,
        c.id_usuario_aprobacion,
        c.fecha_aprobacion,
        c.created_at,
        c.updated_at,
        c.Activo
    FROM finanzas_cotizacion c
    WHERE c.id_cotizacion = p_id_cotizacion
    AND c.Activo = 'S';
    
    -- Items de la cotización
    SELECT
        ci.id_cotizacion_item,
        ci.id_cotizacion,
        ci.item,
        ci.descripcion,
        ci.unidad,
        ci.cantidad,
        ci.precio_unitario,
        ci.subtotal,
        ci.orden,
        ci.seccion,
        ci.created_at,
        ci.updated_at,
        ci.Activo
    FROM finanzas_cotizacion_items ci
    WHERE ci.id_cotizacion = p_id_cotizacion
    AND ci.Activo = 'S'
    ORDER BY ci.orden ASC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_ASIGNAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_ASIGNAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_ASIGNAR`(IN id_usuario_ INT,
    IN cadena_id_perfil TEXT)
BEGIN
    DECLARE count INT DEFAULT 0;
    DECLARE valor1_ VARCHAR(255);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    -- Iniciar transacción
    START TRANSACTION;

    -- PASO 1: Eliminar TODOS los perfiles actuales del usuario
    -- Esto es lo que faltaba en tu versión anterior
    DELETE FROM seguridad_perfil_users 
    WHERE id_usuario = id_usuario_;

    -- PASO 2: Insertar los nuevos perfiles (solo si hay datos)
    IF cadena_id_perfil IS NOT NULL AND cadena_id_perfil != '' THEN
        
        read_loop: LOOP
            SET count = count + 1;
            SET valor1_ = SPLIT_STR(cadena_id_perfil, "|", count);
            
            IF valor1_ = '' OR valor1_ IS NULL THEN
                LEAVE read_loop;
            END IF;

            INSERT INTO seguridad_perfil_users (id_usuario, id_perfil, created_at) 
            VALUES (id_usuario_, valor1_, NOW());
            
        END LOOP read_loop;
        
    END IF;

    -- Confirmar transacción
    COMMIT;

    -- Retornar resultado final
    SELECT 
        spu.id_perfil_users,
        spu.id_usuario,
        spu.id_perfil,
        sp.nombre,
        spu.created_at
    FROM seguridad_perfil_users spu
    INNER JOIN seguridad_perfil sp ON spu.id_perfil = sp.id_perfil
    WHERE spu.id_usuario = id_usuario_;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_ELIMINAR_MULTIPLE
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_ELIMINAR_MULTIPLE`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_ELIMINAR_MULTIPLE`(IN cadena_id_perfil 		TEXT)
BEGIN
-- Referencia: getUpdateWUfromVRFILE
 DECLARE count INT Default 0 ;
 DECLARE valor1_ VARCHAR(255);
 
			
 read_loop: LOOP
         SET count	 = count+1;
         SET valor1_ = SPLIT_STR(cadena_id_perfil,"|",count);
				 
         IF valor1_ ='' THEN
            LEAVE read_loop;
         END IF;

				 DELETE FROM `seguridad_perfil_users` WHERE id_perfil_users = valor1_;
			 
 END LOOP read_loop;
 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_LISTAR`()
BEGIN
    SELECT 
        ROW_NUMBER() OVER (ORDER BY P.id_perfil) AS RowIndex,
        P.id_perfil,
        P.nombre,
        P.created_at,
        P.Activo,
        -- Agrupa los IDs de roles en un JSON array
        (
            SELECT JSON_ARRAYAGG(R.id_roles)
            FROM seguridad_roles_perfil RP
            JOIN seguridad_roles R ON RP.id_roles = R.id_roles
            WHERE RP.id_perfil = P.id_perfil
        ) AS roles_asignados_ids,
        -- Agrupa los NOMBRES de roles en un JSON array
        (
            SELECT JSON_ARRAYAGG(R.nombre)
            FROM seguridad_roles_perfil RP
            JOIN seguridad_roles R ON RP.id_roles = R.id_roles
            WHERE RP.id_perfil = P.id_perfil
        ) AS roles_asignados_nombres
    FROM 
        seguridad_perfil P;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_MENU_LISTAR_TREEVIEW
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_MENU_LISTAR_TREEVIEW`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_MENU_LISTAR_TREEVIEW`(IN p_id_rol INT)
BEGIN
    -- 1. Vaciar tabla temporal
    TRUNCATE TABLE sistema_menu_treeview_temp;
    
    -- 2. Insertar menús principales (nivel 0)
    INSERT INTO sistema_menu_treeview_temp(
        `IdMenu`, `IdMenuPadre`, `IdObjeto`, `nombre`, `Tipo`, 
        `Nivel`, `orden`, `selected`, `Expanded`, `Icono`, `Activo`, `id_menu_objetos`
    )
    SELECT 
        CONCAT('m', m.id_menu) AS IdMenu,
        NULL AS IdMenuPadre,
        NULL AS IdObjeto,
        m.nombre AS Nombre,
        'menu' AS Tipo,
        0 AS Nivel,
        m.orden AS Orden,
        0 AS selected,
        1 AS Expanded,
        COALESCE(m.icono, 'folder') AS Icono,
        m.Activo,
        NULL AS id_menu_objetos
    FROM sistema_menu m
    WHERE m.id_padre IS NULL;

    -- 3. Insertar submenús (nivel 1+)
    INSERT INTO sistema_menu_treeview_temp(
        `IdMenu`, `IdMenuPadre`, `IdObjeto`, `nombre`, `Tipo`, 
        `Nivel`, `orden`, `selected`, `Expanded`, `Icono`, `Activo`, `id_menu_objetos`
    )
    SELECT 
        CONCAT('m', m.id_menu) AS IdMenu,
        CONCAT('m', m.id_padre) AS IdMenuPadre,
        NULL AS IdObjeto,
        m.nombre AS Nombre,
        'menu' AS Tipo,
        1 AS Nivel,
        m.orden AS Orden,
        0 AS selected,
        1 AS Expanded,
        COALESCE(m.icono, 'folder') AS Icono,
        m.Activo,
        NULL AS id_menu_objetos
    FROM sistema_menu m
    WHERE m.id_padre IS NOT NULL;

    -- 4. Insertar objetos asociados a menús
    INSERT INTO sistema_menu_treeview_temp(
        `IdMenu`, `IdMenuPadre`, `IdObjeto`, `nombre`, `Tipo`, 
        `Nivel`, `orden`, `selected`, `Expanded`, `Icono`, `Activo`, `id_menu_objetos`
    )
    SELECT 
        CONCAT('o', mo.id_menu, '-', mo.id_objetos) AS IdMenu,
        CONCAT('m', mo.id_menu) AS IdMenuPadre,
        mo.id_objetos AS IdObjeto,
        COALESCE(o.nombre, 'Sin nombre') AS Nombre,
        'objeto' AS Tipo,
        (SELECT t.Nivel + 1 FROM sistema_menu_treeview_temp t 
         WHERE t.IdMenu = CONCAT('m', mo.id_menu) LIMIT 1) AS Nivel,
        mo.orden AS Orden,
        0 AS selected,
        0 AS Expanded,
        'bullet' AS Icono,
        mo.Activo,
        mo.id_menu_objetos
    FROM sistema_menu_objetos mo
    LEFT JOIN sistema_objetos o ON mo.id_objetos = o.id_objetos
    WHERE mo.Activo = 'S';

    -- 5. Actualizar estado de selección basado en el rol (usando sistema_menu_objetos)
    IF p_id_rol IS NOT NULL THEN
        -- Marcar menús que tienen al menos un objeto asignado al rol
        UPDATE sistema_menu_treeview_temp t
        JOIN (
            SELECT DISTINCT fx.id_menu 
            FROM sistema_menu_objetos fx INNER JOIN sistema_menu_objetos_roles ORS ON fx.id_menu_objetos=ORS.id_menu_objetos
            WHERE ORS.id_roles = p_id_rol AND Activo = 'S'
        ) mo ON t.IdMenu = CONCAT('m', mo.id_menu)
        SET t.selected = 1
        WHERE t.Tipo = 'menu';
        
        -- Marcar objetos específicos asignados al rol
				/*
        UPDATE sistema_menu_treeview_temp t
        JOIN sistema_menu_objetos mo ON t.IdObjeto = mo.id_objetos 
            AND t.id_menu_objetos = mo.id_menu_objetos
        SET t.selected = 1
        WHERE mo.id_roles = p_id_rol 
            AND t.Tipo = 'objeto'
            AND mo.Activo = 'S';*/
						
				UPDATE  sistema_menu_treeview_temp t
        JOIN sistema_menu_objetos mo ON   t.id_menu_objetos = mo.id_menu_objetos AND t.id_menu_objetos = mo.id_menu_objetos
				INNER JOIN sistema_menu_objetos_roles ORS ON mo.id_menu_objetos=ORS.id_menu_objetos
        SET t.selected = 1
        WHERE ORS.id_roles = p_id_rol  AND t.Tipo = 'objeto' AND mo.Activo = 'S';
						
						
    END IF;

    -- 6. Retornar resultados ordenados
    SELECT 
        IdMenu,
        IdMenuPadre,
        IdObjeto,
        Nombre,
        Tipo,
        Nivel,
        Orden,
        selected,
        Expanded,
        Icono,
        Activo,
        id_menu_objetos
    FROM sistema_menu_treeview_temp
    ORDER BY Nivel, Orden, Nombre;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_OBTENER_CHECK
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_OBTENER_CHECK`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_OBTENER_CHECK`(IN id_usuario_ 					INT)
BEGIN
		 
		SELECT 
				ROW_NUMBER() OVER (ORDER BY DM.id_perfil) AS RowIndex
				,DM.id_perfil
				,DM.nombre
				,DM.created_at
				,DM.Activo
		FROM seguridad_perfil DM WHERE DM.id_perfil NOT IN ( SELECT  SP.id_perfil FROM seguridad_perfil SP INNER JOIN seguridad_perfil_users SPU ON SP.id_perfil=SPU.id_perfil	WHERE  SPU.id_usuario = id_usuario_);
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_OBTENER_ELIMINAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_OBTENER_ELIMINAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_OBTENER_ELIMINAR`(IN id_perfil_users_ 					INT)
BEGIN
		 
		DELETE FROM seguridad_perfil_users WHERE  id_perfil_users = id_perfil_users_;
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_PERFIL_OBTENER_LISTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_PERFIL_OBTENER_LISTA`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_PERFIL_OBTENER_LISTA`(IN id_usuario_ 					INT)
BEGIN
	SELECT
	ROW_NUMBER() OVER (ORDER BY SP.id_perfil ) AS RowIndex
		, SP.id_perfil
		, SPU.id_perfil_users
		, SP.nombre
		, SP.created_at
		, SP.updated_at
		, SPU.id_usuario
		, SP.Activo

		FROM
		seguridad_perfil SP 
		INNER JOIN seguridad_perfil_users SPU ON SP.id_perfil=SPU.id_perfil	
		WHERE  SPU.id_usuario = id_usuario_; 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_ASIGNAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_ASIGNAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_ASIGNAR`(IN id_perfil_       INT,
    IN cadena_id_roles  TEXT)
BEGIN
    DECLARE count INT DEFAULT 0;
    DECLARE valor1_ VARCHAR(50);
    
    -- Manejo de errores: si algo falla, deshacemos todo
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    -- 1. Iniciar transacción
    START TRANSACTION;

    -- 2. ELIMINAR TODOS los roles actuales del perfil (esto es CRUCIAL)
    DELETE FROM seguridad_roles_perfil 
    WHERE id_perfil = id_perfil_;

    -- 3. Si no viene cadena vacía o NULL, insertamos los nuevos
    IF (cadena_id_roles IS NOT NULL AND TRIM(cadena_id_roles) != '' AND TRIM(cadena_id_roles) != '|') THEN
        
        read_loop: LOOP
            SET count = count + 1;
            SET valor1_ = SPLIT_STR(cadena_id_roles, "|", count);
            
            -- Salir cuando no haya más valores
            IF valor1_ = '' OR valor1_ IS NULL THEN
                LEAVE read_loop;
            END IF;

            -- Insertar el nuevo rol
            INSERT INTO seguridad_roles_perfil (id_perfil, id_roles, created_at)
            VALUES (id_perfil_, valor1_, NOW());
            
        END LOOP read_loop;
    END IF;

    -- 4. Confirmar todo
    COMMIT;

    -- 5. Devolver los roles actualmente asignados (útil para confirmar en frontend)
    SELECT 
        srp.id_roles_perfil,
        srp.id_perfil,
        srp.id_roles,
        sr.nombre,
        srp.created_at
    FROM seguridad_roles_perfil srp
    INNER JOIN seguridad_roles sr ON srp.id_roles = sr.id_roles
    WHERE srp.id_perfil = id_perfil_
    ORDER BY sr.nombre;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_ASIGNAR_PERMISOS
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_ASIGNAR_PERMISOS`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_ASIGNAR_PERMISOS`(IN id_roles_ INT,
IN cadena_id_menu	LONGTEXT)
BEGIN
-- En produccion si funciona nirmal
-- SET GLOBAL sql_mode = 'NO_ENGINE_SUBSTITUTION';


-- Referencia: getUpdateWUfromVRFILE
 DECLARE count INT Default 0 ;
 DECLARE valor1_ VARCHAR(255);
 --  LAS SESSIONES SIEMPRE DEBEN IR DEBAJO DE LPS DECLATES O SET
 SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION';
 
 DELETE FROM seguridad_roles_menu WHERE id_roles=id_roles_;
 
 read_loop: LOOP
         SET count	 = count+1;
         SET valor1_ = SPLIT_STR(cadena_id_menu,"|",count);
				 
         IF valor1_ ='' THEN
            LEAVE read_loop;
         END IF;

				 INSERT INTO `seguridad_roles_menu`(`id_roles`, `id_menu`) VALUES (id_roles_,valor1_);
			 
 END LOOP read_loop;
 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_ELIMINAR_MULTIPLE
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_ELIMINAR_MULTIPLE`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_ELIMINAR_MULTIPLE`(IN cadena_id_roles_perfil 		TEXT)
BEGIN
-- Referencia: getUpdateWUfromVRFILE
 DECLARE count INT Default 0 ;
 DECLARE valor1_ VARCHAR(255);
 
			
 read_loop: LOOP
         SET count	 = count+1;
         SET valor1_ = SPLIT_STR(cadena_id_roles_perfil,"|",count);
				 
         IF valor1_ ='' THEN
            LEAVE read_loop;
         END IF;

				 DELETE FROM `seguridad_roles_perfil` WHERE id_roles_perfil = valor1_;
			 
 END LOOP read_loop;
 
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_LISTAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_LISTAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_LISTAR`()
BEGIN
    -- Declaración de variables principales
    DECLARE done INT DEFAULT FALSE;
    DECLARE role_id INT;
    DECLARE role_name VARCHAR(255);
    DECLARE role_created DATETIME;
    
    -- Variables para almacenar JSON
    DECLARE menus_json JSON;
    DECLARE menu_names_json JSON;
    
    -- Declaración de cursor para roles
    DECLARE role_cursor CURSOR FOR 
        SELECT id_roles, nombre, created_at 
        FROM seguridad_roles 
        ORDER BY nombre;
    
    -- Manejador para cuando no haya más filas
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- Crear tabla temporal para resultados
    DROP TEMPORARY TABLE IF EXISTS temp_roles_result;
    CREATE TEMPORARY TABLE temp_roles_result (
        id_roles INT,
        nombre_rol VARCHAR(255),
        created_at DATETIME,
        menus_con_objetos JSON,
        menus_asignados JSON
    );
    
    -- Inicializar variables JSON
    SET menus_json = JSON_ARRAY();
    SET menu_names_json = JSON_ARRAY();
    
    -- Abrir cursor principal
    OPEN role_cursor;
    
    -- Bucle para procesar cada rol
    role_loop: LOOP
        FETCH role_cursor INTO role_id, role_name, role_created;
        IF done THEN
            LEAVE role_loop;
        END IF;
        
        -- Resetear variables JSON para cada rol
        SET menus_json = JSON_ARRAY();
        SET menu_names_json = JSON_ARRAY();
        
        -- Procesar menús para este rol
        BEGIN
            -- Variables para cursor de menús
            DECLARE menu_done INT DEFAULT FALSE;
            DECLARE menu_id INT;
            DECLARE menu_name VARCHAR(255);
            DECLARE objects_json JSON;
            
            -- Declarar cursor para menús
            DECLARE menu_cursor CURSOR FOR 
                SELECT DISTINCT M.id_menu, M.nombre
                FROM sistema_menu_objetos_roles RM
                INNER JOIN sistema_menu_objetos MS ON RM.id_menu_objetos = MS.id_menu_objetos
                JOIN sistema_menu M ON MS.id_menu = M.id_menu
                WHERE RM.id_roles = role_id;
                
            -- Manejador para cursor de menús
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET menu_done = TRUE;
            
            -- Abrir cursor de menús
            OPEN menu_cursor;
            
            -- Bucle para procesar cada menú
            menu_loop: LOOP
                FETCH menu_cursor INTO menu_id, menu_name;
                IF menu_done THEN
                    LEAVE menu_loop;
                END IF;
                
                -- Agregar nombre del menú al array simple
                SET menu_names_json = JSON_ARRAY_APPEND(menu_names_json, '$', menu_name);
                
                -- Obtener objetos para este menú
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'id_objeto', O.id_objetos,
                        'nombre_objeto', O.nombre
                    )
                ) INTO objects_json
								#========== ELS SISTEMA sistema_menu_objetos tiene el id_menu y el id_objetos
								#=> se conecta con los nombres de los objetos
								# => El sistema_menu_objetos_roles se conecta con sistema_menu_objetos por medio de su identificador:id_menu_objetos
								#=> el sistema_menu_objetos_roles es llenado por el treeview donde deposita las varibles:id_menu_objetos y el id_roles
								#=> de manera  que un ROL puede tener 1 o mas menus con sus respectivos objetos. El rol puede tener todo el treeview si fuera el caso
                FROM sistema_menu_objetos MO
                INNER JOIN	sistema_objetos O ON MO.id_objetos = O.id_objetos
							  INNER JOIN	sistema_menu_objetos_roles RM   ON MO.id_menu_objetos = RM.id_menu_objetos
                WHERE MO.id_menu = menu_id AND RM.id_roles=role_id;
                
                -- Construir objeto menú completo
                SET menus_json = JSON_ARRAY_APPEND(
                    menus_json, 
                    '$', 
                    JSON_OBJECT(
                        'id_menu', menu_id,
                        'nombre_menu', menu_name,
                        'objetos', COALESCE(objects_json, JSON_ARRAY())
                    )
                );
            END LOOP menu_loop;
            
            -- Cerrar cursor de menús
            CLOSE menu_cursor;
        END;
        
        -- Insertar resultado para este rol
        INSERT INTO temp_roles_result VALUES (
            role_id,
            role_name,
            role_created,
            CASE WHEN JSON_LENGTH(menus_json) > 0 THEN menus_json ELSE JSON_ARRAY() END,
            CASE WHEN JSON_LENGTH(menu_names_json) > 0 THEN menu_names_json ELSE JSON_ARRAY() END
        );
    END LOOP role_loop;
    
    -- Cerrar cursor principal
    CLOSE role_cursor;
    
    -- Retornar resultados
    SELECT * FROM temp_roles_result;
    
    -- Limpiar tabla temporal
    DROP TEMPORARY TABLE IF EXISTS temp_roles_result;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_OBTENER_CHECK
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_OBTENER_CHECK`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_OBTENER_CHECK`(IN id_perfil_ 					INT)
BEGIN
		 
		SELECT 
				ROW_NUMBER() OVER (ORDER BY DM.id_roles) AS RowIndex
				,DM.id_roles
				,DM.nombre
				,DM.created_at
				,DM.Activo
		FROM seguridad_roles DM WHERE DM.id_roles NOT IN ( SELECT  SP.id_roles FROM seguridad_roles_perfil	SP WHERE  SP.id_perfil = id_perfil_);
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_OBTENER_ELIMINAR
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_OBTENER_ELIMINAR`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_OBTENER_ELIMINAR`(IN id_roles_perfil_ 					INT)
BEGIN
		 
		DELETE FROM seguridad_roles_perfil WHERE  id_roles_perfil = id_roles_perfil_;
						
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for USP_SEGURIDAD_ROLES_OBTENER_LISTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `USP_SEGURIDAD_ROLES_OBTENER_LISTA`;
delimiter ;;
CREATE PROCEDURE `USP_SEGURIDAD_ROLES_OBTENER_LISTA`(IN id_perfil_ 					INT)
BEGIN
	SELECT
			ROW_NUMBER() OVER (ORDER BY SP.id_roles ) AS RowIndex
		, SP.id_roles
		, SPU.id_roles_perfil
		, SP.nombre
		, SP.created_at
		, SP.Activo

		FROM
		seguridad_roles SP 
		INNER JOIN seguridad_roles_perfil SPU ON SP.id_roles=SPU.id_roles	
		WHERE  SPU.id_perfil = id_perfil_; 
						
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
