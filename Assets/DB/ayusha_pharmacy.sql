-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.29 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ayusha_pharmacy
CREATE DATABASE IF NOT EXISTS `ayusha_pharmacy` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ayusha_pharmacy`;

-- Dumping structure for table ayusha_pharmacy.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(45) NOT NULL,
  `product_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_category_product_status1_idx` (`product_status_id`),
  CONSTRAINT `fk_category_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.brand: ~4 rows (approximately)
INSERT INTO `brand` (`id`, `brand`, `product_status_id`) VALUES
	(1, 'GlaxoSmithKline', 1),
	(2, 'GSK', 1),
	(3, 'Reckitt Benckiser', 1),
	(4, 'Mundipharma', 1);

-- Dumping structure for table ayusha_pharmacy.cash_withdrawal
CREATE TABLE IF NOT EXISTS `cash_withdrawal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `session_id` int NOT NULL,
  `amount` double NOT NULL,
  `reason` text NOT NULL,
  `date_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cash_withdrawal_session1_idx` (`session_id`),
  CONSTRAINT `fk_cash_withdrawal_session1` FOREIGN KEY (`session_id`) REFERENCES `session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.cash_withdrawal: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.close_sale
CREATE TABLE IF NOT EXISTS `close_sale` (
  `id` int NOT NULL AUTO_INCREMENT,
  `session_id` int NOT NULL,
  `c5000` int NOT NULL,
  `c1000` int NOT NULL,
  `c500` int NOT NULL,
  `c100` int NOT NULL,
  `c50` int NOT NULL,
  `c20` int NOT NULL,
  `c10` int NOT NULL,
  `c5` int NOT NULL,
  `date_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_close_sale_session1_idx` (`session_id`),
  CONSTRAINT `fk_close_sale_session1` FOREIGN KEY (`session_id`) REFERENCES `session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.close_sale: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.company
CREATE TABLE IF NOT EXISTS `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(150) NOT NULL,
  `telephone_1` varchar(10) NOT NULL,
  `telephone_2` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.company: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.costing
CREATE TABLE IF NOT EXISTS `costing` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_product` int NOT NULL,
  `child_product` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_costing_product1_idx` (`parent_product`),
  KEY `fk_costing_product2_idx` (`child_product`),
  CONSTRAINT `fk_costing_product1` FOREIGN KEY (`parent_product`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_costing_product2` FOREIGN KEY (`child_product`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.costing: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.ctrl
CREATE TABLE IF NOT EXISTS `ctrl` (
  `primary_key` varchar(10) NOT NULL,
  `inventory_system` varchar(3) NOT NULL,
  `employee_management` varchar(3) NOT NULL,
  `discount` varchar(3) NOT NULL,
  `refund` varchar(3) NOT NULL,
  `credit_payment` varchar(3) NOT NULL,
  `withdrawal` varchar(3) NOT NULL,
  `stock_adj_count` int NOT NULL COMMENT '1 time per one month',
  `temporary_chance` int NOT NULL,
  `temporary_chance_updated_date` date NOT NULL,
  PRIMARY KEY (`primary_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.ctrl: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mobile` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `credit_amount` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.customer: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.customer_has_invoice
CREATE TABLE IF NOT EXISTS `customer_has_invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `invoice_id` int NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_customer_has_invoice_invoice1_idx` (`invoice_id`),
  KEY `fk_customer_has_invoice_customer1_idx` (`customer_id`),
  CONSTRAINT `fk_customer_has_invoice_customer1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `fk_customer_has_invoice_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.customer_has_invoice: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.damage
CREATE TABLE IF NOT EXISTS `damage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `reason` text NOT NULL,
  `employee_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_damage_employee1_idx` (`employee_id`),
  CONSTRAINT `fk_damage_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.damage: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.damage_item
CREATE TABLE IF NOT EXISTS `damage_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` double NOT NULL,
  `damage_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_damage_item_product1_idx` (`product_id`),
  KEY `fk_damage_item_damage1_idx` (`damage_id`),
  CONSTRAINT `fk_damage_item_damage1` FOREIGN KEY (`damage_id`) REFERENCES `damage` (`id`),
  CONSTRAINT `fk_damage_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.damage_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.discount_type
CREATE TABLE IF NOT EXISTS `discount_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL COMMENT 'percentage, cash amount',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.discount_type: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.distribute_type
CREATE TABLE IF NOT EXISTS `distribute_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL COMMENT 'primary, secondary',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.distribute_type: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `employee_role_id` int NOT NULL,
  `employee_status_id` int NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employee_employee_role1_idx` (`employee_role_id`),
  KEY `fk_employee_employee_status1_idx` (`employee_status_id`),
  CONSTRAINT `fk_employee_employee_role1` FOREIGN KEY (`employee_role_id`) REFERENCES `employee_role` (`id`),
  CONSTRAINT `fk_employee_employee_status1` FOREIGN KEY (`employee_status_id`) REFERENCES `employee_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.employee: ~0 rows (approximately)
INSERT INTO `employee` (`id`, `name`, `employee_role_id`, `employee_status_id`, `username`, `password`) VALUES
	(1, 'Vihaga Heshan', 2, 1, 'Cashier', '$argon2i$v=19$m=65536,t=10,p=4$AvccWTN7r6poTvSXh8UgmA$mOoCOKhQcjfOkhXyjGgz4p8YI4WDBAfBlMju/zVbHWs'),
	(2, 'Vihanga Heshan', 1, 1, 'Admin', '$argon2i$v=19$m=65536,t=10,p=4$AvccWTN7r6poTvSXh8UgmA$mOoCOKhQcjfOkhXyjGgz4p8YI4WDBAfBlMju/zVbHWs');

-- Dumping structure for table ayusha_pharmacy.employee_role
CREATE TABLE IF NOT EXISTS `employee_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  `employee_role_type_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employee_role_employee_role_type1_idx` (`employee_role_type_id`),
  CONSTRAINT `fk_employee_role_employee_role_type1` FOREIGN KEY (`employee_role_type_id`) REFERENCES `employee_role_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.employee_role: ~2 rows (approximately)
INSERT INTO `employee_role` (`id`, `role`, `employee_role_type_id`) VALUES
	(1, 'admin', 1),
	(2, 'cashier', 2);

-- Dumping structure for table ayusha_pharmacy.employee_role_has_interface
CREATE TABLE IF NOT EXISTS `employee_role_has_interface` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_role_id` int NOT NULL,
  `interface_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employee_role_has_interface_employee_role1_idx` (`employee_role_id`),
  CONSTRAINT `fk_employee_role_has_interface_employee_role1` FOREIGN KEY (`employee_role_id`) REFERENCES `employee_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.employee_role_has_interface: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.employee_role_type
CREATE TABLE IF NOT EXISTS `employee_role_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.employee_role_type: ~2 rows (approximately)
INSERT INTO `employee_role_type` (`id`, `type`) VALUES
	(1, 'admin'),
	(2, 'cashier');

-- Dumping structure for table ayusha_pharmacy.employee_status
CREATE TABLE IF NOT EXISTS `employee_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL COMMENT 'active, inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.employee_status: ~2 rows (approximately)
INSERT INTO `employee_status` (`id`, `status`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping structure for table ayusha_pharmacy.grn
CREATE TABLE IF NOT EXISTS `grn` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grn_code` varchar(20) NOT NULL,
  `date_time` datetime NOT NULL,
  `supplier_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_supplier1_idx` (`supplier_id`),
  CONSTRAINT `fk_grn_supplier1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.grn: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.grn_item
CREATE TABLE IF NOT EXISTS `grn_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `grn_id` int NOT NULL,
  `cost_price` double NOT NULL,
  `qty` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_item_product1_idx` (`product_id`),
  KEY `fk_grn_item_grn1_idx` (`grn_id`),
  CONSTRAINT `fk_grn_item_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `fk_grn_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.grn_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `bill_amount` double NOT NULL,
  `paid_amount` double NOT NULL,
  `credit_amount` double NOT NULL,
  `session_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_session1_idx` (`session_id`),
  CONSTRAINT `fk_invoice_session1` FOREIGN KEY (`session_id`) REFERENCES `session` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.invoice: ~0 rows (approximately)
INSERT INTO `invoice` (`id`, `date_time`, `bill_amount`, `paid_amount`, `credit_amount`, `session_id`) VALUES
	(12, '2025-06-05 11:52:32', 4650, 5000, 0, 4);

-- Dumping structure for table ayusha_pharmacy.invoice_item
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` double NOT NULL,
  `sale_price` double NOT NULL,
  `cost_price` double NOT NULL,
  `invoice_id` int NOT NULL,
  `invoice_item_type_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_item_product1_idx` (`product_id`),
  KEY `fk_invoice_item_invoice1_idx` (`invoice_id`),
  KEY `fk_invoice_item_invoice_item_type1_idx` (`invoice_item_type_id`),
  CONSTRAINT `fk_invoice_item_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `fk_invoice_item_invoice_item_type1` FOREIGN KEY (`invoice_item_type_id`) REFERENCES `invoice_item_type` (`id`),
  CONSTRAINT `fk_invoice_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.invoice_item: ~0 rows (approximately)
INSERT INTO `invoice_item` (`id`, `product_id`, `qty`, `sale_price`, `cost_price`, `invoice_id`, `invoice_item_type_id`) VALUES
	(26, 2, 8, 450, 380, 12, 1),
	(27, 3, 3, 350, 280, 12, 1);

-- Dumping structure for table ayusha_pharmacy.invoice_item_type
CREATE TABLE IF NOT EXISTS `invoice_item_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL COMMENT 'selling, returning   ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.invoice_item_type: ~2 rows (approximately)
INSERT INTO `invoice_item_type` (`id`, `type`) VALUES
	(1, 'Sell'),
	(2, 'Return');

-- Dumping structure for table ayusha_pharmacy.license
CREATE TABLE IF NOT EXISTS `license` (
  `id` int NOT NULL AUTO_INCREMENT,
  `period` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL COMMENT 'purchased, pending\nif a customer purchase a system, status should be purchased.\nuntil then, status will be pending.\nif a customer purchase the system for life-time license, status should be completed.',
  `charge` double NOT NULL COMMENT 'license price',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.license: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.location
CREATE TABLE IF NOT EXISTS `location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `telephone_1` varchar(10) NOT NULL,
  `telephone_2` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.location: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.location_return
CREATE TABLE IF NOT EXISTS `location_return` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `location_return_type_id` int NOT NULL,
  `employee_id` int NOT NULL,
  `location_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_location_return_location_return_type1_idx` (`location_return_type_id`),
  KEY `fk_location_return_employee1_idx` (`employee_id`),
  KEY `fk_location_return_location1_idx` (`location_id`),
  CONSTRAINT `fk_location_return_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_location_return_location1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`),
  CONSTRAINT `fk_location_return_location_return_type1` FOREIGN KEY (`location_return_type_id`) REFERENCES `location_return_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.location_return: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.location_return_item
CREATE TABLE IF NOT EXISTS `location_return_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `location_return_id` int NOT NULL,
  `qty` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_location_return_item_product1_idx` (`product_id`),
  KEY `fk_location_return_item_location_return1_idx` (`location_return_id`),
  CONSTRAINT `fk_location_return_item_location_return1` FOREIGN KEY (`location_return_id`) REFERENCES `location_return` (`id`),
  CONSTRAINT `fk_location_return_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.location_return_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.location_return_type
CREATE TABLE IF NOT EXISTS `location_return_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL COMMENT 'defauld, damage, expired\ndefault return goes to store table\ndamage & expired goes to damage table',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.location_return_type: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product` varchar(100) NOT NULL,
  `generic_name` varchar(100) DEFAULT NULL,
  `sale_price` double NOT NULL,
  `cost_price` double NOT NULL,
  `discount` double NOT NULL,
  `measure` float NOT NULL,
  `bar_code` varchar(45) DEFAULT NULL,
  `product_unit_id` int NOT NULL,
  `brand_id` int NOT NULL,
  `product_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_product_status1_idx` (`product_status_id`),
  KEY `fk_product_product_unit1_idx` (`product_unit_id`),
  KEY `fk_product_brand1_idx` (`brand_id`),
  CONSTRAINT `fk_product_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_product_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`),
  CONSTRAINT `fk_product_product_unit1` FOREIGN KEY (`product_unit_id`) REFERENCES `product_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product: ~4 rows (approximately)
INSERT INTO `product` (`id`, `product`, `generic_name`, `sale_price`, `cost_price`, `discount`, `measure`, `bar_code`, `product_unit_id`, `brand_id`, `product_status_id`) VALUES
	(1, 'Panadol Tablet', 'Paracetamol', 5, 2.5, 0, 1, NULL, 1, 1, 1),
	(2, 'Ventolin Inhaler', 'Ventolin Inhaler', 450, 380, 0, 1, NULL, 1, 2, 1),
	(3, 'Dettol Antiseptic Liquid', NULL, 350, 280, 0, 1, NULL, 1, 3, 1),
	(4, 'Betadine Solution', 'Povidone-Iodine', 220, 180, 0, 1, NULL, 1, 4, 1),
	(5, 'asd', NULL, 123, 100, 0, 1, NULL, 1, 2, 1);

-- Dumping structure for table ayusha_pharmacy.product_distribute
CREATE TABLE IF NOT EXISTS `product_distribute` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` varchar(45) NOT NULL,
  `employee_id` int NOT NULL,
  `receiver` varchar(50) NOT NULL,
  `distribute_type_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_distribute_employee1_idx` (`employee_id`),
  KEY `fk_product_distribute_distribute_type1_idx` (`distribute_type_id`),
  CONSTRAINT `fk_product_distribute_distribute_type1` FOREIGN KEY (`distribute_type_id`) REFERENCES `distribute_type` (`id`),
  CONSTRAINT `fk_product_distribute_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_distribute: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.product_distribute_has_location
CREATE TABLE IF NOT EXISTS `product_distribute_has_location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_distribute_id` int NOT NULL,
  `location_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_issue_has_location_location1_idx` (`location_id`),
  KEY `fk_product_issue_has_location_product_issue1_idx` (`product_distribute_id`),
  CONSTRAINT `fk_product_issue_has_location_location1` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`),
  CONSTRAINT `fk_product_issue_has_location_product_issue1` FOREIGN KEY (`product_distribute_id`) REFERENCES `product_distribute` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_distribute_has_location: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.product_distribute_item
CREATE TABLE IF NOT EXISTS `product_distribute_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` double NOT NULL,
  `product_distribute_id` int NOT NULL,
  `product_price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_distribute_item_product1_idx` (`product_id`),
  KEY `fk_product_distribute_item_product_distribute1_idx` (`product_distribute_id`),
  CONSTRAINT `fk_product_distribute_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_distribute_item_product_distribute1` FOREIGN KEY (`product_distribute_id`) REFERENCES `product_distribute` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_distribute_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.product_has_product_type
CREATE TABLE IF NOT EXISTS `product_has_product_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `product_type_id` int NOT NULL,
  `reference_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_has_product_type_product1_idx` (`product_id`),
  KEY `fk_product_has_product_type_product_type1_idx` (`product_type_id`),
  KEY `fk_product_has_product_type_product2_idx` (`reference_id`),
  CONSTRAINT `fk_product_has_product_type_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_has_product_type_product2` FOREIGN KEY (`reference_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_product_has_product_type_product_type1` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_has_product_type: ~0 rows (approximately)
INSERT INTO `product_has_product_type` (`id`, `product_id`, `product_type_id`, `reference_id`) VALUES
	(1, 5, 1, 5);

-- Dumping structure for table ayusha_pharmacy.product_status
CREATE TABLE IF NOT EXISTS `product_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL COMMENT 'active, inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_status: ~2 rows (approximately)
INSERT INTO `product_status` (`id`, `status`) VALUES
	(1, 'Enable'),
	(2, 'Disable');

-- Dumping structure for table ayusha_pharmacy.product_type
CREATE TABLE IF NOT EXISTS `product_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(10) NOT NULL COMMENT 'parent, child',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_type: ~2 rows (approximately)
INSERT INTO `product_type` (`id`, `type`) VALUES
	(1, 'Parent'),
	(2, 'Child');

-- Dumping structure for table ayusha_pharmacy.product_unit
CREATE TABLE IF NOT EXISTS `product_unit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `unit` varchar(45) DEFAULT NULL COMMENT 'g, ml, units, piece',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.product_unit: ~2 rows (approximately)
INSERT INTO `product_unit` (`id`, `unit`) VALUES
	(1, 'item'),
	(2, 'mg'),
	(3, 'ml');

-- Dumping structure for table ayusha_pharmacy.refund
CREATE TABLE IF NOT EXISTS `refund` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `comission` double NOT NULL,
  `refund_amount` double NOT NULL,
  `refund_status_id` int NOT NULL,
  `session_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_refund_refund_status1_idx` (`refund_status_id`),
  KEY `fk_refund_session1_idx` (`session_id`),
  CONSTRAINT `fk_refund_refund_status1` FOREIGN KEY (`refund_status_id`) REFERENCES `refund_status` (`id`),
  CONSTRAINT `fk_refund_session1` FOREIGN KEY (`session_id`) REFERENCES `session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.refund: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.refund_item
CREATE TABLE IF NOT EXISTS `refund_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` double NOT NULL,
  `refund_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_refund_item_product1_idx` (`product_id`),
  KEY `fk_refund_item_refund1_idx` (`refund_id`),
  CONSTRAINT `fk_refund_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_refund_item_refund1` FOREIGN KEY (`refund_id`) REFERENCES `refund` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.refund_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.refund_status
CREATE TABLE IF NOT EXISTS `refund_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL COMMENT 'default, damage',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.refund_status: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.session
CREATE TABLE IF NOT EXISTS `session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day_in_time` datetime NOT NULL,
  `day_out_time` datetime DEFAULT NULL,
  `petty_cash` double NOT NULL,
  `collection` double DEFAULT NULL,
  `employee_id` int NOT NULL,
  `status` varchar(3) DEFAULT NULL COMMENT 'ON & OFF',
  PRIMARY KEY (`id`),
  KEY `fk_session_employee_idx` (`employee_id`),
  CONSTRAINT `fk_session_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.session: ~1 rows (approximately)
INSERT INTO `session` (`id`, `day_in_time`, `day_out_time`, `petty_cash`, `collection`, `employee_id`, `status`) VALUES
	(4, '2025-06-05 11:50:16', '2025-06-05 12:02:27', 45000, 125000, 1, 'OFF');

-- Dumping structure for table ayusha_pharmacy.stock
CREATE TABLE IF NOT EXISTS `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` double NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_product1_idx` (`product_id`),
  CONSTRAINT `fk_stock_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.stock: ~0 rows (approximately)
INSERT INTO `stock` (`id`, `qty`, `product_id`) VALUES
	(1, 0, 5);

-- Dumping structure for table ayusha_pharmacy.stock_adjustment
CREATE TABLE IF NOT EXISTS `stock_adjustment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `employee_id` int NOT NULL,
  `location` varchar(45) NOT NULL COMMENT 'main store (stock table)\nmain cashier (stock table)',
  `reason` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_adjustment_employee1_idx` (`employee_id`),
  CONSTRAINT `fk_stock_adjustment_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.stock_adjustment: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.stock_adjustment_item
CREATE TABLE IF NOT EXISTS `stock_adjustment_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `stock_adjustment_id` int NOT NULL,
  `qty` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_adjustment_item_product1_idx` (`product_id`),
  KEY `fk_stock_adjustment_item_stock_adjustment1_idx` (`stock_adjustment_id`),
  CONSTRAINT `fk_stock_adjustment_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_stock_adjustment_item_stock_adjustment1` FOREIGN KEY (`stock_adjustment_id`) REFERENCES `stock_adjustment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.stock_adjustment_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.store
CREATE TABLE IF NOT EXISTS `store` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` double NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_store_product1_idx` (`product_id`),
  CONSTRAINT `fk_store_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.store: ~0 rows (approximately)
INSERT INTO `store` (`id`, `qty`, `product_id`) VALUES
	(1, 0, 5);

-- Dumping structure for table ayusha_pharmacy.supplier
CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `telephone` varchar(10) NOT NULL,
  `supplier_status_id` int NOT NULL,
  `company_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplier_supplier_status1_idx` (`supplier_status_id`),
  KEY `fk_supplier_company1_idx` (`company_id`),
  CONSTRAINT `fk_supplier_company1` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `fk_supplier_supplier_status1` FOREIGN KEY (`supplier_status_id`) REFERENCES `supplier_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supplier_damage_return
CREATE TABLE IF NOT EXISTS `supplier_damage_return` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `supplier_id` int NOT NULL,
  `reason` text NOT NULL,
  `supply_damage_return_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplier_damage_return_supplier1_idx` (`supplier_id`),
  KEY `fk_supplier_damage_return_supply_damage_return_status1_idx` (`supply_damage_return_status_id`),
  CONSTRAINT `fk_supplier_damage_return_supplier1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  CONSTRAINT `fk_supplier_damage_return_supply_damage_return_status1` FOREIGN KEY (`supply_damage_return_status_id`) REFERENCES `supply_damage_return_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier_damage_return: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supplier_damage_return_item
CREATE TABLE IF NOT EXISTS `supplier_damage_return_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` double NOT NULL,
  `return_price` double NOT NULL,
  `product_id` int NOT NULL,
  `supplier_damage_return_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplier_damage_return_item_product1_idx` (`product_id`),
  KEY `fk_supplier_damage_return_item_supplier_damage_return1_idx` (`supplier_damage_return_id`),
  CONSTRAINT `fk_supplier_damage_return_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_supplier_damage_return_item_supplier_damage_return1` FOREIGN KEY (`supplier_damage_return_id`) REFERENCES `supplier_damage_return` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier_damage_return_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supplier_order
CREATE TABLE IF NOT EXISTS `supplier_order` (
  `id` int NOT NULL,
  `supplier_id` int NOT NULL,
  `required_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplier_order_supplier1_idx` (`supplier_id`),
  CONSTRAINT `fk_supplier_order_supplier1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier_order: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supplier_order_item
CREATE TABLE IF NOT EXISTS `supplier_order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` double NOT NULL,
  `supplier_order_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplier_order_item_supplier_order1_idx` (`supplier_order_id`),
  KEY `fk_supplier_order_item_product1_idx` (`product_id`),
  CONSTRAINT `fk_supplier_order_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_supplier_order_item_supplier_order1` FOREIGN KEY (`supplier_order_id`) REFERENCES `supplier_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier_order_item: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supplier_status
CREATE TABLE IF NOT EXISTS `supplier_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL COMMENT 'active, inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supplier_status: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.supply_damage_return_status
CREATE TABLE IF NOT EXISTS `supply_damage_return_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL COMMENT 'pending, returned',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.supply_damage_return_status: ~0 rows (approximately)

-- Dumping structure for table ayusha_pharmacy.system
CREATE TABLE IF NOT EXISTS `system` (
  `primary_key` varchar(10) NOT NULL,
  `system_name` varchar(45) NOT NULL,
  `initial_date` date NOT NULL,
  `deactive_date` datetime NOT NULL,
  `telephone_1` varchar(10) NOT NULL,
  `telephone_2` varchar(10) NOT NULL,
  `address` varchar(100) NOT NULL,
  `discount_amount` double NOT NULL,
  `discount_percentage` double NOT NULL,
  `discount_range` double NOT NULL,
  `discount_type_id` int NOT NULL,
  `license_id` int NOT NULL,
  PRIMARY KEY (`primary_key`),
  KEY `fk_system_discount_type1_idx` (`discount_type_id`),
  KEY `fk_system_license1_idx` (`license_id`),
  CONSTRAINT `fk_system_discount_type1` FOREIGN KEY (`discount_type_id`) REFERENCES `discount_type` (`id`),
  CONSTRAINT `fk_system_license1` FOREIGN KEY (`license_id`) REFERENCES `license` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table ayusha_pharmacy.system: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
