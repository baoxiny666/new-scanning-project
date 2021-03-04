package com.tglh.newscanningproject.scanning.service;

import com.tglh.newscanningproject.scanning.entity.Banner;
import com.tglh.newscanningproject.scanning.entity.DepartMent;
import com.tglh.newscanningproject.scanning.entity.Notice;
import com.tglh.newscanningproject.scanning.entity.SafeModules;

import java.util.List;

public interface HomeService {
      List<SafeModules> modules();
      List<Banner> banner();
      Notice notice();

}
