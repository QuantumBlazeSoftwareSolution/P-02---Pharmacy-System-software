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

-- Dumping data for table ayusha_pharmacy.brand: ~25 rows (approximately)
REPLACE INTO `brand` (`id`, `brand`, `product_status_id`) VALUES
	(1, 'Central Nervous System', 1),
	(2, 'High Alert', 1),
	(3, 'Others', 1),
	(4, 'Baby Product', 1),
	(5, 'Cosmetics', 1),
	(6, 'Sanitary', 1),
	(7, 'Oncology', 1),
	(8, 'Vetenary', 1),
	(9, 'Cardio Vascular System', 1),
	(10, 'Endocrine System', 1),
	(11, 'Eye & Ear Product', 1),
	(12, 'Antibiotics', 1),
	(13, 'Gastro Intestinal System', 1),
	(14, 'Grocery', 1),
	(15, 'Musculoskeletal & Joint Product', 1),
	(16, 'Malignant Product', 1),
	(17, 'Nutrition Product', 1),
	(18, 'Oropharynx', 1),
	(19, 'Immunologycal Product', 1),
	(20, 'Respiratory System', 1),
	(21, 'Skin Product', 1),
	(22, 'Surgical Product & Devices', 1),
	(23, 'Herbal Product', 1),
	(24, 'Gynaecology Product', 1),
	(25, 'Narcotics', 1);

-- Dumping data for table ayusha_pharmacy.cash_withdrawal: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.close_sale: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.company: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.costing: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.ctrl: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.customer: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.customer_has_invoice: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.damage: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.damage_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.discount_type: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.distribute_type: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.employee: ~2 rows (approximately)
REPLACE INTO `employee` (`id`, `name`, `employee_role_id`, `employee_status_id`, `username`, `password`) VALUES
	(1, 'Milani Wijewardhana', 2, 1, 'Minu', '$argon2i$v=19$m=65536,t=10,p=4$YO0u8leBg+6rrTB56TfPgw$3ZaB8+F45B+53rob/k44zCA7XStlNpU0dcGHBd2Me8Q'),
	(2, 'Milani Wijewardhana', 1, 1, 'Ayusha', '$argon2i$v=19$m=65536,t=10,p=4$wfX29KjUEvW7kD6qBC+0FQ$WqbSALsuO9NW2qE3sSUlJlbBT9Qrh+DP/ClRx4XXoCU');

-- Dumping data for table ayusha_pharmacy.employee_role: ~2 rows (approximately)
REPLACE INTO `employee_role` (`id`, `role`, `employee_role_type_id`) VALUES
	(1, 'admin', 1),
	(2, 'cashier', 2);

-- Dumping data for table ayusha_pharmacy.employee_role_has_interface: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.employee_role_type: ~2 rows (approximately)
REPLACE INTO `employee_role_type` (`id`, `type`) VALUES
	(1, 'admin'),
	(2, 'cashier');

-- Dumping data for table ayusha_pharmacy.employee_status: ~2 rows (approximately)
REPLACE INTO `employee_status` (`id`, `status`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping data for table ayusha_pharmacy.grn: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.grn_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.invoice: ~2 rows (approximately)
REPLACE INTO `invoice` (`id`, `date_time`, `bill_amount`, `paid_amount`, `credit_amount`, `session_id`) VALUES
	(1, '2025-06-26 07:14:38', 58.44, 100, 0, 1),
	(2, '2025-06-26 07:19:44', 48.7, 50, 0, 1);

-- Dumping data for table ayusha_pharmacy.invoice_item: ~2 rows (approximately)
REPLACE INTO `invoice_item` (`id`, `product_id`, `qty`, `sale_price`, `discount`, `cost_price`, `invoice_id`, `invoice_item_type_id`) VALUES
	(1, 1, 6, 9.74, 0, 8.47, 1, 1),
	(2, 1, 5, 9.74, 0, 8.47, 2, 1);

-- Dumping data for table ayusha_pharmacy.invoice_item_type: ~2 rows (approximately)
REPLACE INTO `invoice_item_type` (`id`, `type`) VALUES
	(1, 'Sell'),
	(2, 'Return');

-- Dumping data for table ayusha_pharmacy.license: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.location: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.location_return: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.location_return_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.location_return_type: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.product: ~111 rows (approximately)
REPLACE INTO `product` (`id`, `product`, `generic_name`, `sale_price`, `cost_price`, `discount`, `measure`, `bar_code`, `product_unit_id`, `brand_id`, `product_status_id`) VALUES
	(1, 'Penicilline 250mg', 'Oral Penicilline', 9.74, 8.47, 0, 1, NULL, 5, 12, 1),
	(2, 'Cloxil 250mg', 'Cloxacillin', 16, 10.55, 0, 1, NULL, 6, 12, 1),
	(3, 'Cloxil 500mg', 'Cloxacillin', 36, 23.74, 0, 1, NULL, 6, 12, 1),
	(4, 'Axcil 250mg', 'Amoxicillin', 20, 15.83, 0, 1, NULL, 6, 12, 1),
	(5, 'Axcil 500mg', 'Amoxicillin', 30, 23.74, 0, 1, NULL, 6, 12, 1),
	(6, 'Amoxil 500mg', 'Amoxicillin', 49.97, 43.08, 0, 1, NULL, 6, 12, 1),
	(7, 'Blucef 125mg', 'Cephalexine', 21.37, 18.26, 0, 1, NULL, 6, 12, 1),
	(8, 'Augmentin 625mg', 'Amoxicillin & Clavalenic Acid', 119.16, 102.76, 0, 1, NULL, 6, 12, 1),
	(9, 'Aumentin 375mg', 'Amoycillin & Clavalenic acid', 70.31, 60.64, 0, 1, NULL, 6, 12, 1),
	(10, 'clavamox 625mg', 'Amoxycillin & Clavalenic acid', 98.7, 78.03, 0, 1, NULL, 6, 12, 1),
	(11, 'Clavamox 375mg', 'Amoycillin & Clavalenic acid', 69.7, 60.61, 0, 1, NULL, 6, 12, 1),
	(12, 'Enhanci 625mg', 'Amoxycillin & Clavalenic acid', 119.1, 94.16, 0, 1, NULL, 6, 12, 1),
	(13, 'Enhancin 375mg', 'Amoxycillin & Clavalenic Acid', 66.95, 54.58, 0, 1, NULL, 6, 12, 1),
	(14, 'Novaclav 625mg', 'Amoxycillin & Clavalenic Acid', 99.17, 85.46, 0, 1, NULL, 6, 12, 1),
	(15, 'Enhancin Sy', 'amoxycillin & Clavalenic Acid', 570, 450.61, 0, 1, NULL, 1, 12, 1),
	(16, 'Augmentin Sy', 'Amoxycillin & Clavalenic acid', 571.02, 519.11, 0, 1, NULL, 1, 12, 1),
	(17, 'Bactoclav Sy', 'Amoxycillin & Clavalenic acid', 562.8, 474.72, 0, 1, NULL, 1, 12, 1),
	(18, 'Amoxil Sy', 'Amoxycillin', 501, 432.36, 0, 1, NULL, 1, 12, 1),
	(19, 'Axcil Sy', 'Amoxycillline', 465, 368, 0, 1, NULL, 1, 12, 1),
	(20, 'Augmntin sy', 'Amoxycillin & Clavalenic acid', 571.02, 519.11, 0, 1, NULL, 1, 12, 1),
	(21, 'Cephadex Sy', 'Cephalexin', 296, 254.91, 0, 1, NULL, 1, 12, 1),
	(22, 'Cephadex 500mg', 'Cephalexin', 19.86, 17.12, 0, 1, NULL, 6, 12, 1),
	(23, 'Cephaldex 250mg', 'Cephlexin', 16.27, 14.03, 0, 1, NULL, 6, 12, 1),
	(24, 'Sporidex 250mg', 'Cephalexin', 16.28, 14.16, 0, 1, NULL, 6, 12, 1),
	(25, 'Sporidex 500mg', 'Cephalexin', 29.8, 25.91, 0, 1, NULL, 6, 12, 1),
	(26, 'Cephast 250mg', 'Cephalexin', 15.28, 13.29, 0, 1, NULL, 6, 12, 1),
	(27, 'Cephast 500mg', 'Cephalexin', 28.86, 25.1, 0, 1, NULL, 6, 12, 1),
	(28, 'Cephast Sy', 'Cephalexin', 388, 337.4, 0, 1, NULL, 6, 12, 1),
	(29, 'Sporidex Sy', 'Cephalexin', 525, 456.54, 0, 1, NULL, 6, 12, 1),
	(30, 'Zinnat 500mg', 'Cefuroxin', 144.19, 124.35, 0, 1, NULL, 5, 12, 1),
	(31, 'Zinnat 250mg', 'Cefuroxin', 90.37, 77.72, 0, 1, NULL, 5, 12, 1),
	(32, 'Zinnat 125mg', 'Cefuroxin', 79.88, 68.92, 0, 1, NULL, 5, 12, 1),
	(33, 'Zinnat Sy', 'Cefuroxin', 2043.36, 1764.65, 0, 1, NULL, 1, 12, 1),
	(34, 'Cefakind 500mg', 'Cefuroxin', 122.6, 98.41, 0, 1, NULL, 5, 12, 1),
	(35, 'Cefakind 250mg', 'Cefuroxin', 75.6, 65.74, 0, 1, NULL, 5, 12, 1),
	(36, 'Cefasyn 500mg', 'Cefuroxin', 128.5, 110.78, 0, 1, NULL, 5, 12, 1),
	(37, 'Cefasyn 250mg', 'Cefuroxin', 83.2, 71.7, 0, 1, NULL, 5, 12, 1),
	(38, 'Cefayn 125mg', 'Cefuroxin', 64.9, 55.9, 0, 1, NULL, 5, 12, 1),
	(39, 'Tetraleb 250mg', 'Tetracyclline', 12.5, 10.68, 0, 1, NULL, 6, 12, 1),
	(40, 'Microdox 100mg', 'Doxycyclline', 47.88, 40.56, 0, 1, NULL, 6, 12, 1),
	(41, 'Rimycin 100mg', 'Doxycycllin', 20, 17.39, 0, 1, NULL, 6, 12, 1),
	(42, 'Doxyn 100mg', 'Doxycycllin', 20.05, 17.44, 0, 1, NULL, 6, 12, 1),
	(43, 'Erythrocin 500mg', 'Erythromycin', 83.62, 71.47, 0, 1, NULL, 5, 12, 1),
	(44, 'Erythrocin 250mg', 'Erythromycin ', 55.25, 47.22, 0, 1, NULL, 5, 12, 1),
	(45, 'Erythrocin Sy', 'Erythromycin', 913, 793.94, 0, 1, NULL, 1, 12, 1),
	(46, 'Claritek 500mg', 'Clarithromycin', 104.9, 91.22, 0, 1, NULL, 5, 12, 1),
	(47, 'Claritek 250mg ', 'Clarithromycin', 66.3, 57.65, 0, 1, NULL, 5, 12, 1),
	(48, 'Claritek Sy', 'Clarithromycin', 798, 693.91, 0, 1, NULL, 1, 12, 1),
	(49, 'Azee 500mg', 'Azithromycin', 104.7, 90.25, 0, 1, NULL, 5, 12, 1),
	(50, 'Azee 250mg', 'Azithromycin', 52.67, 45.54, 0, 1, NULL, 5, 12, 1),
	(51, 'Zithrin 500mg', 'Azithromycin', 104.78, 91.12, 0, 1, NULL, 6, 12, 1),
	(52, 'Zithrin 250mg', 'Azithromycin', 74.68, 64.94, 0, 1, NULL, 6, 12, 1),
	(53, 'Zithrin Sy 15ml', 'Azithromycin', 347.58, 302.26, 0, 1, NULL, 1, 12, 1),
	(54, 'Zithrin Sy 30ml ', 'Azithromycin', 703.48, 601.26, 0, 1, NULL, 1, 12, 1),
	(55, 'ATM Sy', 'Azithromyciin', 346, 300.87, 0, 1, NULL, 1, 12, 1),
	(56, 'L - Trim 480mg', 'Co Trimaxol', 11.5, 9.83, 0, 1, NULL, 5, 12, 1),
	(57, 'L Trim Sy', 'Co Trimaxole', 360, 307.69, 0, 1, NULL, 1, 12, 1),
	(58, 'Metrogyl 400mg', 'Metronidazole', 22.29, 19.05, 0, 1, NULL, 5, 12, 1),
	(59, 'Metrogyl 200mg', 'Metranidazole', 12.04, 10.29, 0, 1, NULL, 5, 12, 1),
	(60, 'Metrogyl Sy', 'Metranidazole', 768, 656.41, 0, 1, NULL, 1, 12, 1),
	(61, 'Ciprobid 500mg', 'Ciprofloxacin', 16.88, 14.68, 0, 1, NULL, 5, 12, 1),
	(62, 'Ciprobid 250mg', 'Ciprofloxacin', 11.42, 9.93, 0, 1, NULL, 5, 12, 1),
	(63, 'Ciprolet 500mg', 'Ciprofloxacin', 16.88, 14.68, 0, 1, NULL, 5, 12, 1),
	(64, 'Noroxin 400mg', 'Norfloxacin', 159.96, 137.31, 0, 1, NULL, 5, 12, 1),
	(65, 'Climycin 150mg', 'Clindamycin', 83.62, 71.47, 0, 1, NULL, 6, 12, 1),
	(66, 'Climycin 300mg', 'Clindamycin', 116.1, 99.23, 0, 1, NULL, 6, 12, 1),
	(67, 'Dalacin C150mg', 'Clindamycin', 83.62, 71.47, 0, 1, NULL, 6, 12, 1),
	(68, 'Dalacin C 300mg', 'Clindamycin', 116.1, 99.23, 0, 1, NULL, 6, 12, 1),
	(69, 'Flubiotic 500mg', 'Flucloxacilin', 55, 47.83, 0, 1, NULL, 6, 12, 1),
	(70, 'Flubiotic 250mg', 'Flucloxacilin ', 40, 34.78, 0, 1, NULL, 6, 12, 1),
	(71, 'Phylopen 500mg', 'Flucloxacillin', 74.3, 63.5, 0, 1, NULL, 6, 12, 1),
	(72, 'Phylopen 250mg', 'Flucloxacillin', 46.45, 36.65, 0, 1, NULL, 6, 12, 1),
	(73, 'Phylopen Sy', 'Flucloxacillin', 709.92, 606, 0, 1, NULL, 1, 12, 1),
	(74, 'Nitrofurantion 100mg', NULL, 1.64, 1.4, 0, 1, NULL, 5, 12, 1),
	(75, 'Nitrofurantion 50mg', 'Nitrofurantion MSJ', 2.16, 1.84, 0, 1, NULL, 5, 12, 1),
	(76, 'Mahacef 100mg', 'Cefexim', 37.85, 32.91, 0, 1, NULL, 5, 12, 1),
	(77, 'Mahacef 200mg', 'Cefexim', 71.62, 62.28, 0, 1, NULL, 5, 12, 1),
	(78, 'Duracef Sy', 'Cefexim', 400, 347.83, 0, 1, NULL, 1, 12, 1),
	(79, 'Leflox 250mg', 'Levofloxacin', 34.3, 29.82, 0, 1, NULL, 5, 12, 1),
	(80, 'Leflox 500mg', 'Levofloxacin', 56.7, 49.3, 0, 1, NULL, 6, 12, 1),
	(81, 'Fungicon 150mg', 'Flucanazole', 85.02, 72.02, 0, 1, NULL, 6, 12, 1),
	(82, 'Omastin 150mg', 'Flucanazole', 85.12, 74.02, 0, 1, NULL, 6, 12, 1),
	(83, 'Omastin 50mg', 'Flucanazole', 47.28, 41.11, 0, 1, NULL, 6, 12, 1),
	(84, 'Griseben 500m', 'Griseofulvin', 46.8, 40, 0, 1, NULL, 6, 12, 1),
	(85, 'Terbiderm 250mg', 'Terbinaforse', 164.7, 140.75, 0, 1, NULL, 6, 12, 1),
	(86, 'Terbinaforce 250mg', 'Terbinaf', 176.47, 150.83, 0, 1, NULL, 6, 12, 1),
	(87, 'Itracon 100mg', 'Itraconazole', 121.5, 100.73, 0, 1, NULL, 6, 12, 1),
	(88, 'Icon 100mg', 'Itraconazole', 113.05, 96.62, 0, 1, NULL, 6, 12, 1),
	(89, 'Unitrac 100mg', 'Itraconazole', 275.5, 235.47, 0, 1, NULL, 6, 12, 1),
	(90, 'Cyclovior 200mg', 'Acyclovior', 20, 17.34, 0, 1, NULL, 6, 12, 1),
	(91, 'HCQs 200mg', 'Hydroxychloroquine', 75.34, 64.95, 0, 1, NULL, 6, 12, 1),
	(92, 'Sazo En 500mg', 'Sulpersalazin', 33.5, 28.8, 0, 1, NULL, 6, 12, 1),
	(93, 'Vermox 500mg', 'Mebandazole', 390, 331.5, 0, 1, NULL, 5, 12, 1),
	(94, 'Ver,mox 100mg', 'Mebandazole', 464, 394.4, 0, 1, NULL, 6, 12, 1),
	(95, 'Wormin 500mg', 'Mebandazole', 83.6, 71.45, 0, 1, NULL, 6, 12, 1),
	(96, 'Wormin 100mg', 'mebandazole', 70, 59.82, 0, 1, NULL, 5, 12, 1),
	(97, 'Zental 200mg', 'Albendazole', 139.32, 120.11, 0, 1, NULL, 5, 12, 1),
	(98, 'Pyrantin', 'Pyrntal', 16, 12.66, 0, 1, NULL, 6, 12, 1),
	(99, 'Pyrantin sY', 'Pyrental', 270, 213.7, 0, 1, NULL, 1, 12, 1),
	(100, 'Nefin ER 20mg', 'Nefidifine', 3.07, 2.23, 0, 1, NULL, 5, 9, 1),
	(101, 'Nicardia 20mg', 'Nifidifine', 5.58, 4.85, 0, 1, NULL, 5, 9, 1),
	(102, 'Nicardia XL 30mg', 'Nifidifine', 53.39, 45.63, 0, 1, NULL, 5, 9, 1),
	(103, 'Nicaria 10mg', 'Nifidifine', 14.25, 12.18, 0, 1, NULL, 5, 9, 1),
	(104, 'Verapamil 40mg', 'Verapamil SPMC', 2.39, 2.08, 0, 1, NULL, 5, 9, 1),
	(105, 'Dilcardia SR 90mg', 'Dilteazem', 42.48, 36.94, 0, 1, NULL, 5, 9, 1),
	(106, 'Dilzem SR 90mg', 'Dilteazem', 40.61, 35.31, 0, 1, NULL, 5, 9, 1),
	(107, 'Dilzem 30mg', 'Dilteazem', 5.12, 4.45, 0, 1, NULL, 5, 9, 1),
	(108, '                        Dilzem 60mg                   ', 'Dilteazem', 7.09, 6.17, 0, 1, NULL, 5, 9, 1),
	(109, 'Zem XL 90mg', 'Dilteazem', 22.57, 19.63, 0, 1, NULL, 5, 9, 1),
	(110, 'Tenalol 50mg', 'Atenalol', 6, 5.21, 0, 1, NULL, 5, 9, 1),
	(111, 'Propranalol 40mg', 'Proparanalol SPMC', 1.4, 1.22, 0, 1, NULL, 5, 9, 1),
	(112, 'Carvil 3.125mg', 'Carvedilol', 10.45, 8.93, 0, 1, NULL, 5, 9, 1),
	(113, 'Carvil 6.25mg', 'Carvedilol', 10.45, 8.93, 0, 1, NULL, 5, 9, 1),
	(114, 'Carvil12.5mg', 'Carvedilol', 21.41, 18.3, 0, 1, NULL, 5, 9, 1),
	(115, 'Concor 2.5mg', 'Bisoprolol', 36, 30, 0, 1, NULL, 5, 9, 1),
	(116, 'Concor 5mg', 'Bisoprolol', 60, 50, 0, 1, NULL, 5, 9, 1),
	(117, 'Bisovic 2.5mg', 'Bisoprolol', 25.43, 21.73, 0, 1, NULL, 5, 9, 1),
	(118, 'Bisovic 05mg', 'Bisoprolo', 38.75, 33.12, 0, 1, NULL, 5, 9, 1);

-- Dumping data for table ayusha_pharmacy.product_distribute: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.product_distribute_has_location: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.product_distribute_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.product_has_product_type: ~111 rows (approximately)
REPLACE INTO `product_has_product_type` (`id`, `product_id`, `product_type_id`, `reference_id`) VALUES
	(1, 1, 1, 1),
	(2, 2, 1, 2),
	(3, 3, 1, 3),
	(4, 4, 1, 4),
	(5, 5, 1, 5),
	(6, 6, 1, 6),
	(7, 7, 1, 7),
	(8, 8, 1, 8),
	(9, 9, 1, 9),
	(10, 10, 1, 10),
	(11, 11, 1, 11),
	(12, 12, 1, 12),
	(13, 13, 1, 13),
	(14, 14, 1, 14),
	(15, 15, 1, 15),
	(16, 16, 1, 16),
	(17, 17, 1, 17),
	(18, 18, 1, 18),
	(19, 19, 1, 19),
	(20, 20, 1, 20),
	(21, 21, 1, 21),
	(22, 22, 1, 22),
	(23, 23, 1, 23),
	(24, 24, 1, 24),
	(25, 25, 1, 25),
	(26, 26, 1, 26),
	(27, 27, 1, 27),
	(28, 28, 1, 28),
	(29, 29, 1, 29),
	(30, 30, 1, 30),
	(31, 31, 1, 31),
	(32, 32, 1, 32),
	(33, 33, 1, 33),
	(34, 34, 1, 34),
	(35, 35, 1, 35),
	(36, 36, 1, 36),
	(37, 37, 1, 37),
	(38, 38, 1, 38),
	(39, 39, 1, 39),
	(40, 40, 1, 40),
	(41, 41, 1, 41),
	(42, 42, 1, 42),
	(43, 43, 1, 43),
	(44, 44, 1, 44),
	(45, 45, 1, 45),
	(46, 46, 1, 46),
	(47, 47, 1, 47),
	(48, 48, 1, 48),
	(49, 49, 1, 49),
	(50, 50, 1, 50),
	(51, 51, 1, 51),
	(52, 52, 1, 52),
	(53, 53, 1, 53),
	(54, 54, 1, 54),
	(55, 55, 1, 55),
	(56, 56, 1, 56),
	(57, 57, 1, 57),
	(58, 58, 1, 58),
	(59, 59, 1, 59),
	(60, 60, 1, 60),
	(61, 61, 1, 61),
	(62, 62, 1, 62),
	(63, 63, 1, 63),
	(64, 64, 1, 64),
	(65, 65, 1, 65),
	(66, 66, 1, 66),
	(67, 67, 1, 67),
	(68, 68, 1, 68),
	(69, 69, 1, 69),
	(70, 70, 1, 70),
	(71, 71, 1, 71),
	(72, 72, 1, 72),
	(73, 73, 1, 73),
	(74, 74, 1, 74),
	(75, 75, 1, 75),
	(76, 76, 1, 76),
	(77, 77, 1, 77),
	(78, 78, 1, 78),
	(79, 79, 1, 79),
	(80, 80, 1, 80),
	(81, 81, 1, 81),
	(82, 82, 1, 82),
	(83, 83, 1, 83),
	(84, 84, 1, 84),
	(85, 85, 1, 85),
	(86, 86, 1, 86),
	(87, 87, 1, 87),
	(88, 88, 1, 88),
	(89, 89, 1, 89),
	(90, 90, 1, 90),
	(91, 91, 1, 91),
	(92, 92, 1, 92),
	(93, 93, 1, 93),
	(94, 94, 1, 94),
	(95, 95, 1, 95),
	(96, 96, 1, 96),
	(97, 97, 1, 97),
	(98, 98, 1, 98),
	(99, 99, 1, 99),
	(100, 100, 1, 100),
	(101, 101, 1, 101),
	(102, 102, 1, 102),
	(103, 103, 1, 103),
	(104, 104, 1, 104),
	(105, 105, 1, 105),
	(106, 106, 1, 106),
	(107, 107, 1, 107),
	(108, 108, 1, 108),
	(109, 109, 1, 109),
	(110, 110, 1, 110),
	(111, 111, 1, 111),
	(112, 112, 1, 112),
	(113, 113, 1, 113),
	(114, 114, 1, 114),
	(115, 115, 1, 115),
	(116, 116, 1, 116),
	(117, 117, 1, 117),
	(118, 118, 1, 118);

-- Dumping data for table ayusha_pharmacy.product_status: ~2 rows (approximately)
REPLACE INTO `product_status` (`id`, `status`) VALUES
	(1, 'Enable'),
	(2, 'Disable');

-- Dumping data for table ayusha_pharmacy.product_type: ~2 rows (approximately)
REPLACE INTO `product_type` (`id`, `type`) VALUES
	(1, 'Parent'),
	(2, 'Child');

-- Dumping data for table ayusha_pharmacy.product_unit: ~8 rows (approximately)
REPLACE INTO `product_unit` (`id`, `unit`) VALUES
	(1, 'PIECE (PCS)'),
	(2, 'MILLIGRAM (MG)'),
	(3, 'MILLILITER (ML)'),
	(4, 'GRAM (G)'),
	(5, 'TAB (T)'),
	(6, 'CAPSULE (C)'),
	(7, 'MICROGRAM (MCG)'),
	(8, 'SUPPOSITORY (S)');

-- Dumping data for table ayusha_pharmacy.refund: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.refund_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.refund_status: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.session: ~1 rows (approximately)
REPLACE INTO `session` (`id`, `day_in_time`, `day_out_time`, `petty_cash`, `collection`, `employee_id`, `status`) VALUES
	(1, '2025-06-26 06:50:25', NULL, 6500, NULL, 1, 'ON'),
	(2, '2025-06-27 03:42:58', NULL, 6500, NULL, 1, 'ON'),
	(3, '2025-06-28 05:58:31', '2025-06-28 06:00:25', 5000, 15000, 1, 'OFF'),
	(4, '2025-07-02 09:23:31', NULL, 50000, NULL, 1, 'ON');

-- Dumping data for table ayusha_pharmacy.stock: ~118 rows (approximately)
REPLACE INTO `stock` (`id`, `qty`, `product_id`) VALUES
	(1, 0, 1),
	(2, 0, 2),
	(3, 0, 3),
	(4, 0, 4),
	(5, 0, 5),
	(6, 0, 6),
	(7, 0, 7),
	(8, 0, 8),
	(9, 0, 9),
	(10, 0, 10),
	(11, 0, 11),
	(12, 0, 12),
	(13, 0, 13),
	(14, 0, 14),
	(15, 0, 15),
	(16, 0, 16),
	(17, 0, 17),
	(18, 0, 18),
	(19, 0, 19),
	(20, 0, 20),
	(21, 0, 21),
	(22, 0, 22),
	(23, 0, 23),
	(24, 0, 24),
	(25, 0, 25),
	(26, 0, 26),
	(27, 0, 27),
	(28, 0, 28),
	(29, 0, 29),
	(30, 0, 30),
	(31, 0, 31),
	(32, 0, 32),
	(33, 0, 33),
	(34, 0, 34),
	(35, 0, 35),
	(36, 0, 36),
	(37, 0, 37),
	(38, 0, 38),
	(39, 0, 39),
	(40, 0, 40),
	(41, 0, 41),
	(42, 0, 42),
	(43, 0, 43),
	(44, 0, 44),
	(45, 0, 45),
	(46, 0, 46),
	(47, 0, 47),
	(48, 0, 48),
	(49, 0, 49),
	(50, 0, 50),
	(51, 0, 51),
	(52, 0, 52),
	(53, 0, 53),
	(54, 0, 54),
	(55, 0, 55),
	(56, 0, 56),
	(57, 0, 57),
	(58, 0, 58),
	(59, 0, 59),
	(60, 0, 60),
	(61, 0, 61),
	(62, 0, 62),
	(63, 0, 63),
	(64, 0, 64),
	(65, 0, 65),
	(66, 0, 66),
	(67, 0, 67),
	(68, 0, 68),
	(69, 0, 69),
	(70, 0, 70),
	(71, 0, 71),
	(72, 0, 72),
	(73, 0, 73),
	(74, 0, 74),
	(75, 0, 75),
	(76, 0, 76),
	(77, 0, 77),
	(78, 0, 78),
	(79, 0, 79),
	(80, 0, 80),
	(81, 0, 81),
	(82, 0, 82),
	(83, 0, 83),
	(84, 0, 84),
	(85, 0, 85),
	(86, 0, 86),
	(87, 0, 87),
	(88, 0, 88),
	(89, 0, 89),
	(90, 0, 90),
	(91, 0, 91),
	(92, 0, 92),
	(93, 0, 93),
	(94, 0, 94),
	(95, 0, 95),
	(96, 0, 96),
	(97, 0, 97),
	(98, 0, 98),
	(99, 0, 99),
	(100, 0, 100),
	(101, 0, 101),
	(102, 0, 102),
	(103, 0, 103),
	(104, 0, 104),
	(105, 0, 105),
	(106, 0, 106),
	(107, 0, 107),
	(108, 0, 108),
	(109, 0, 109),
	(110, 0, 110),
	(111, 0, 111),
	(112, 0, 112),
	(113, 0, 113),
	(114, 0, 114),
	(115, 0, 115),
	(116, 0, 116),
	(117, 0, 117),
	(118, 0, 118);

-- Dumping data for table ayusha_pharmacy.stock_adjustment: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.stock_adjustment_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.store: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier_damage_return: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier_damage_return_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier_order: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier_order_item: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.supplier_status: ~2 rows (approximately)
REPLACE INTO `supplier_status` (`id`, `status`) VALUES
	(1, 'Active'),
	(2, 'Inactive');

-- Dumping data for table ayusha_pharmacy.supply_damage_return_status: ~0 rows (approximately)

-- Dumping data for table ayusha_pharmacy.system: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
