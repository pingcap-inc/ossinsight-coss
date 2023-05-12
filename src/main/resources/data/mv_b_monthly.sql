CREATE TABLE `mv_b_monthly` (
    `github_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `event_month` date NOT NULL,
    `star_num` int(11) DEFAULT NULL,
    `pr_num` int(11) DEFAULT NULL,
    `fork_num` int(11) DEFAULT NULL,
    PRIMARY KEY (`github_name`,`event_month`) /*T![clustered_index] NONCLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;