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
import com.pingcap.ossinsightcoss.dao.COSSDevMonthlyBean;
import com.pingcap.ossinsightcoss.dao.COSSDevMonthlyRepository;
import com.pingcap.ossinsightcoss.util.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * COSSDevMonthlyController
 *
 * @author Icemap
 * @date 2022/10/29
 */
@RestController
@RequestMapping("/monthly")
public class COSSDevMonthlyController {
    @Autowired
    COSSDevMonthlyRepository cossDevMonthlyRepository;
    @Autowired
    FileUtil fileUtil;

    @GetMapping("/all")
    public Object getAll() {
        return cossDevMonthlyRepository.findAll();
    }

    @GetMapping("/csv")
    public void getCSV(HttpServletResponse response) throws IOException {
        String csvContent = cossDevMonthlyRepository.findAll()
                .stream().map(COSSDevMonthlyBean::toCSVLine)
                .collect(Collectors.joining("\n"));
        csvContent = COSSDevMonthlyBean.getCSVHeader() + "\n" + csvContent;
        fileUtil.returnFile(response, "monthly.csv", csvContent);
    }
}
