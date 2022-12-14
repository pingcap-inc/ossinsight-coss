// Copyright 2022 PingCAP, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.pingcap.ossinsightcoss.controller;

import com.pingcap.ossinsightcoss.dao.COSSDevDailyBean;
import com.pingcap.ossinsightcoss.dao.COSSDevDailyRepository;
import com.pingcap.ossinsightcoss.util.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * COSSDevDailyController
 *
 * @author Icemap
 * @date 2022/10/24
 */
@RestController
@RequestMapping("/daily")
public class COSSDevDailyController {
    @Autowired
    COSSDevDailyRepository cossDevDailyRepository;
    @Autowired
    FileUtil fileUtil;

    @GetMapping("/csv")
    public void getCSV(HttpServletResponse response) throws IOException {
        String csvContent = cossDevDailyRepository.findAll()
                .stream().map(COSSDevDailyBean::toCSVLine)
                .collect(Collectors.joining("\n"));
        csvContent = COSSDevDailyBean.getCSVHeader() + "\n" + csvContent;
        fileUtil.returnFile(response, "daily.csv", csvContent);
    }
}
