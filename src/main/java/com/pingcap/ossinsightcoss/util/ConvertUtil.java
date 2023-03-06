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

package com.pingcap.ossinsightcoss.util;

import com.pingcap.ossinsightcoss.dao.BaldertonTrackedBean;
import com.pingcap.ossinsightcoss.dao.COSSInvestBean;
import com.pingcap.ossinsightcoss.dao.COSSInvestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * ConvertUtil
 *
 * @author Icemap
 * @date 2022/10/20
 */
@Component
public class ConvertUtil {
    @Autowired
    FileUtil fileUtil;
    @Autowired
    COSSInvestRepository cossInvestRepository;

    private static final int DATA_LENGTH = 13;
    private static final int BALDERTON_DATA_LENGTH = 2;

    SimpleDateFormat normalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public COSSInvestBean convertFromCSV (String csvLine) throws Exception {
        // "id","company","repository","stage","round_size","year","month","lead_investor","has_repo","has_github","github_name","date","link"
        // "1","Acryl Data","https://github.com/datahub-project/datahub","Seed","9","2021","6","8VC","1","1","datahub-project/datahub","1/6/2021 00:00:00","https://techcrunch.com/2021/06/23/acryl-data-commercializing-linkedins-metadata-tool-datahub-emerges-from-stealth-with-9m-from-8vc-linkedin-and-insight/"
        COSSInvestBean result = new COSSInvestBean();

        String[] params = csvLine.split("\",\"");
        if (params.length != 0) {
            params[0] = params[0].substring(1);
            params[params.length - 1] = params[params.length - 1]
                    .substring(0, params[params.length - 1].length() - 1);
        }

        if (params.length != DATA_LENGTH) {
            throw new Exception("data error");
        }

        result.setId(Integer.parseInt(params[0]));
        result.setCompany(params[1]);
        result.setRepository(params[2]);
        result.setStage(COSSInvestBean.Stage.valueOf(params[3]));
        result.setRoundSize(new BigDecimal(params[4]));
        result.setYear(Year.parse(params[5]));
        result.setMonth(Integer.parseInt(params[6]));
        result.setLeadInvestor(params[7]);
        result.setHasRepo(Integer.parseInt(params[8]) == 1);
        result.setHasGithub(Integer.parseInt(params[9]) == 1);
        result.setGithubName(params[10]);
        result.setDate(normalFormat.parse(params[11]));
        result.setLink(params[12]);

        return result;
    }

    public List<COSSInvestBean> readCOSSInvestBean() throws Exception {
        List<String> cossCSVList = fileUtil.readCOSSInvest();
        List<COSSInvestBean> beanList = new ArrayList<>();

        for (String line: cossCSVList) {
            beanList.add(convertFromCSV(line));
        }

        return beanList;
    }

    public BaldertonTrackedBean convertBaldertonTrackedFromCSV (String csvLine) throws Exception {
        // "company_website","repo_name"
        // "https://surrealdb.com/","surrealdb/surrealdb"
        BaldertonTrackedBean result = new BaldertonTrackedBean();

        String[] params = csvLine.split(",");
        if (params.length != BALDERTON_DATA_LENGTH) {
            throw new Exception("data error");
        }

        result.setRepoName(params[0]);
        result.setCompanyWebsite(params[1]);

        return result;
    }

    public List<BaldertonTrackedBean> readBaldertonTrackedBean() throws Exception {
        List<String> baldertonCSVList = fileUtil.readBaldertonTracked();
        List<BaldertonTrackedBean> beanList = new ArrayList<>();

        for (String line: baldertonCSVList) {
            beanList.add(convertBaldertonTrackedFromCSV(line));
        }

        return beanList;
    }
}
