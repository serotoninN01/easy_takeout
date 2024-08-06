package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    TurnoverReportVO getTurnoverStat(LocalDate begin, LocalDate end);

    UserReportVO getUserStat(LocalDate begin, LocalDate end);
}
