-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2025 at 04:03 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `statpod`
--

-- --------------------------------------------------------

--
-- Table structure for table `genres`
--

CREATE TABLE `genres` (
  `GenreID` int(11) NOT NULL,
  `GenreName` varchar(100) NOT NULL,
  `Description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `genres`
--

INSERT INTO `genres` (`GenreID`, `GenreName`, `Description`) VALUES
(1, 'Comedy', 'Humorous content including stand-up and funny discussions'),
(2, 'Technology', 'Tech news, gadgets, and digital innovations'),
(3, 'Music', 'Artist interviews and music industry content'),
(4, 'Business', 'Entrepreneurship, finance, and corporate strategies'),
(5, 'Education', 'Learning and academic topics'),
(6, 'Horror', 'Scary stories and paranormal content'),
(7, 'True Crime', 'Real crime cases and investigations'),
(8, 'History', 'Historical events and figures'),
(9, 'Science', 'Scientific discoveries and research'),
(10, 'Health And Fitness', 'Wellness, exercise, and nutrition'),
(11, 'Fiction', 'Scripted stories and audio dramas'),
(12, 'Lifestyle', 'Personal development and daily living'),
(13, 'Interview', 'Conversations with notable people.');

-- --------------------------------------------------------

--
-- Table structure for table `podcasts`
--

CREATE TABLE `podcasts` (
  `PodcastID` int(11) NOT NULL,
  `Podcast_Name` varchar(100) NOT NULL,
  `HostName` varchar(100) NOT NULL,
  `ReleaseDate` date DEFAULT NULL,
  `GenreID` int(11) DEFAULT NULL,
  `Description` text DEFAULT NULL,
  `Podimg` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `podcasts`
--

INSERT INTO `podcasts` (`PodcastID`, `Podcast_Name`, `HostName`, `ReleaseDate`, `GenreID`, `Description`, `Podimg`) VALUES
(1, 'Aspects of History', 'Ollie Webb-Carter', '2023-10-26', 8, '\"Aspects of History\" is a captivating podcast that delves into the multifaceted narratives of our past, exploring the events, figures, and cultures that have shaped the world we live in today. Each episode features engaging discussions with historians, authors, and experts, who bring to life the stories behind significant historical moments and lesser-known tales alike. From ancient civilizations to modern conflicts, the podcast uncovers the complexities of human experience, offering listeners a deeper understanding of how history influences contemporary society. Join us as we journey through time, examining the lessons learned and the legacies left behind, all while sparking curiosity and inspiring a passion for history in every episode.', 'aspects_of_history.jpeg'),
(2, 'BBC Sounds', 'BBC Studios', '2023-11-15', 5, 'A collection of podcasts from the BBC.', 'bbc_sounds.jpg'),
(3, 'Conan O\' Brien Needs a Friend', 'Team Coco', '2024-01-03', 1, '\'Conon Needs a Friend\' is a heartwarming and whimsical tale that follows Conon, a quirky and imaginative character who longs for companionship in a world that feels a bit too big and lonely. As Conon embarks on a charming adventure to find a friend, he encounters a variety of colorful characters and learns valuable lessons about friendship, acceptance, and the joy of connection. This delightful story is perfect for readers of all ages, reminding us that sometimes, all it takes is a little courage and creativity to forge meaningful bonds.', 'Conan_OBrian_Podcast.jpg'),
(4, 'Cults', 'Parcast', '2023-12-01', 7, 'Explores the history and psychology of cults.', 'Cults.jpeg'),
(5, 'How I Built This', 'NPR', '2024-02-22', 4, 'Interviews with entrepreneurs about how they built their companies.', 'how_i_built_this.jpg'),
(6, 'The Joe Rogan Experience', 'Joe Rogan', '2024-03-10', 13, 'Long-form interviews on a variety of topics.', 'joeroganexperience.png'),
(7, 'Lore', 'Aaron Mahnke', '2024-09-18', 8, 'Explores the dark side of history and folklore.', 'lore.jpg'),
(8, 'The NoSleep Podcast', 'Creative Media', '2023-10-05', 6, 'Horror fiction podcast.', 'no_sleep_podcast.jpg'),
(9, 'Stone Cold Podcast', 'Steve Austin', '2024-02-01', 13, 'Interviews and discussions with wrestling personalities.', 'stone_cold_podcast.jpg'),
(10, 'Talk is Jericho', 'Chris Jericho', '2024-01-12', 13, 'Interviews with wrestlers and musicians.', 'talk_is_jericho.jpeg'),
(11, 'Tech News Weekly', 'TWiT.tv', '2023-11-29', 4, 'Weekly news about technology.', 'tech_news_weekly.jpg'),
(12, 'The Zane Lowe Show', 'Apple Music', '2024-03-01', 3, 'Music interviews and discussions.', 'zane_lowe_show.jpg'),
(17, 'Borasca', 'QCode', '2025-05-06', 11, 'The Borasca podcast is a gripping audio journey that delves into the mysterious and often chilling world of true crime, urban legends, and unsolved mysteries. Hosted by a team of passionate storytellers, each episode weaves together immersive narratives, expert interviews, and atmospheric soundscapes to transport listeners into the heart of each enigma. With a focus on lesser-known cases and the psychological intricacies behind them, Borasca invites its audience to explore the darker corners of human nature and the secrets that lie beneath the surface of everyday life. Whether you\'re a true crime aficionado or simply curious about the unknown, Borasca promises to captivate and intrigue with every episode.', 'borasca.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `userpodcasts`
--

CREATE TABLE `userpodcasts` (
  `Username` varchar(50) NOT NULL,
  `PodcastID` int(11) NOT NULL,
  `Play` int(100) NOT NULL DEFAULT 0,
  `Liked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userpodcasts`
--

INSERT INTO `userpodcasts` (`Username`, `PodcastID`, `Play`, `Liked`) VALUES
('Someone', 5, 3, 1),
('Someone', 6, 16, 1),
('Someone', 7, 11, 1),
('Someone', 12, 8, 0),
('SydAt', 7, 6, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `Username` varchar(50) NOT NULL,
  `DisplayName` varchar(100) NOT NULL,
  `Email_ID` varchar(100) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `ImageUrl` varchar(255) DEFAULT NULL,
  `FavoriteGenre` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`Username`, `DisplayName`, `Email_ID`, `Password`, `ImageUrl`, `FavoriteGenre`) VALUES
('admin', 'admin', 'admin@mail.com', 'j679azgPmLiVZEHC3VcJLczITDaVRV1XEJkEzhy8LCQl0htgsTaT22iFwYB09L6ljOw6uOZxzRYp', 'admin.jpg', 12),
('Alias', 'Alias', 'alias@mail.com', 'tgJJ2qzAP1cPkmeeXYuKmA7tX71eH5ivuY7ysHv27kMcvmJnk99GYxMXZkjgPR/GN8su4N+SoTpn', 'all-hindu-gods.jpg', 11),
('Alt', 'alt', 'alt@alt.com', 'XrkvSK8hwMpiuPPKtwaaEVzinJCghM0eweUelitSP0YuNw7Tt3L91qCfolxs67wteOIHlgM2ChQp', '20230214_164240.jpg', 3),
('Joe', 'Joe Diamore', 'joe@mail.com', '5+uF9bFmAhD7e8cZw6uBXogtIcMQ68xtuHIpsGuARyccRx9Mjf0mWwzOXx1/r5Dyt5BboTe6ral6', 'S17E19_500.png', 6),
('Pops', 'Pops', 'pops@gmail.com', 'TBEREAb1X4jBHWlchOgE3bbTO/J/98Ksik8qtgAwVKBu4yo3uMoEil2FhfQPRvb/6TvtTW6LSpTy', 'all-hindu-gods.jpg', 1),
('Someone', 'Something Someone', 'something@some.com', 'YasDLhI6cpIviQYQ9Yx71iueTAVajQgtVgOPvxab5TBZM01eiQ9QDr7LiGp1v23o6ybk8G1j78QD', 'all-hindu-gods.jpg', 2),
('SydAt', 'SydAt ', 'sydat@outlook.com', 'tEQQEbKJSZFueWqpZDqcbis7tQArqO7rkIpz18ZpMYVcqFBdtfN3Jay9HJXl1jLHhErn0aXF2ulX', '2023.jpg', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `genres`
--
ALTER TABLE `genres`
  ADD PRIMARY KEY (`GenreID`);

--
-- Indexes for table `podcasts`
--
ALTER TABLE `podcasts`
  ADD PRIMARY KEY (`PodcastID`),
  ADD KEY `GenreID` (`GenreID`);

--
-- Indexes for table `userpodcasts`
--
ALTER TABLE `userpodcasts`
  ADD PRIMARY KEY (`Username`,`PodcastID`),
  ADD KEY `PodcastID` (`PodcastID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Username`),
  ADD UNIQUE KEY `Email_ID` (`Email_ID`),
  ADD KEY `FavoriteGenre` (`FavoriteGenre`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `podcasts`
--
ALTER TABLE `podcasts`
  MODIFY `PodcastID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `podcasts`
--
ALTER TABLE `podcasts`
  ADD CONSTRAINT `podcasts_ibfk_1` FOREIGN KEY (`GenreID`) REFERENCES `genres` (`GenreID`);

--
-- Constraints for table `userpodcasts`
--
ALTER TABLE `userpodcasts`
  ADD CONSTRAINT `userpodcasts_ibfk_1` FOREIGN KEY (`Username`) REFERENCES `users` (`Username`),
  ADD CONSTRAINT `userpodcasts_ibfk_2` FOREIGN KEY (`PodcastID`) REFERENCES `podcasts` (`PodcastID`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`FavoriteGenre`) REFERENCES `genres` (`GenreID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
