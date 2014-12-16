/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.DyLine;
import org.xclcharts.renderer.info.Legend;
import org.xclcharts.renderer.line.PlotDot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @ClassName BarChart01View
 * @Description  柱形图例子(竖向) 
 *  
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class BarChart01View extends DemoView implements Runnable{ //DemoView
	
	private String TAG = "BarChart01View";
	private BarChart chart = new BarChart();
	
	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	
	Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
	PlotDot mDotToolTip = new PlotDot();
			
	
	public BarChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();				
	}
	
	public BarChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public BarChart01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		 	chartLabels();
			chartDataSet();
			chartRender();
			new Thread(this).start();
	 }
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h); // + w * 0.5f
    }  
		 	
	private void chartRender()
	{
		try {								
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);		
						
			//数据源
			chart.setDataSource(chartData);
			chart.setCategories(chartLabels);	
			
			
			//轴标题
			chart.getAxisTitle().setLeftAxisTitle("数据库");
			chart.getAxisTitle().setLowerAxisTitle("分布位置");
			
			//数据轴
			chart.getDataAxis().setAxisMax(100);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(5);
			
			//定义数据轴标签显示格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub		
					Double tmp = Double.parseDouble(value);
					DecimalFormat df=new DecimalFormat("#0");
					String label = df.format(tmp).toString();				
					return (label);
				}
				
			});
			
			//在柱形顶部显示值
			chart.getBar().setItemLabelVisible(true);
			
		
			//设定格式
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df=new DecimalFormat("#0");					 
					String label = df.format(value).toString();
					return label;
				}});
			
			 //让柱子间没空白
			 //chart.getBar().setBarInnerMargin(0d);
		
			
			//轴颜色
			int axisColor = Color.BLUE; // Color.rgb(222, 136, 166);			
			chart.getDataAxis().getAxisPaint().setColor(axisColor);
			chart.getCategoryAxis().getAxisPaint().setColor(axisColor);			
			chart.getDataAxis().getTickMarksPaint().setColor(axisColor);
			chart.getCategoryAxis().getTickMarksPaint().setColor(axisColor);
								
			//指隔多少个轴刻度(即细刻度)后为主刻度
			chart.getDataAxis().setDetailModeSteps(5);
			
			//扩展横向显示范围,当数据太多时可用这个扩展实际绘图面积
			chart.getPlotArea().extWidth(200f);
		  			
  			
			//显示十字交叉线
			chart.showDyLine();
			DyLine dyl = chart.getDyLine();
			if( null != dyl)
			{
				dyl.setDyLineStyle(XEnum.DyLineStyle.Horizontal);
				dyl.setLineDrawStyle(XEnum.LineStyle.DASH);
			}
			
			//数据轴居中显示
			//chart.setDataAxisLocation(XEnum.AxisLocation.VERTICAL_CENTER);
			
			//不使用精确计算，忽略Java计算误差
			chart.disableHighPrecision();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	private void chartDataSet()
	{
		//标签对应的柱形数据集
		List<Double> dataSeriesA= new LinkedList<Double>();	
		dataSeriesA.add(66d); 
		dataSeriesA.add(33d); 
		dataSeriesA.add(50d);
		BarData BarDataA = new BarData("Oracle",dataSeriesA,(int)Color.rgb(186, 20, 26));
		
		
		List<Double> dataSeriesB= new LinkedList<Double>();	
		dataSeriesB.add(32d);
		dataSeriesB.add(22d);
		dataSeriesB.add(18d);
		BarData BarDataB = new BarData("SQL Server",dataSeriesB,(int)Color.rgb(1, 188, 242));
		
		List<Double> dataSeriesC= new LinkedList<Double>();	
		dataSeriesC.add(79d);
		dataSeriesC.add(91d);
		dataSeriesC.add(65d);
		BarData BarDataC = new BarData("MySQL",dataSeriesC,(int)Color.rgb(0, 75, 106)); 
		
		List<Double> dataSeriesD= new LinkedList<Double>();	
		dataSeriesD.add(52d);
		dataSeriesD.add(45d);
		dataSeriesD.add(35d);
		BarData BarDataD = new BarData("Other",dataSeriesD,(int)Color.rgb(17, 3, 111)); 
		
		
		chartData.add(BarDataA);
		chartData.add(BarDataB);
		chartData.add(BarDataC);
		chartData.add(BarDataD);
	}
	
	private void chartLabels()
	{
		chartLabels.add("福田数据中心"); 
		chartLabels.add("西丽数据中心"); 
		chartLabels.add("观澜数据中心"); 
	}	
		
	@Override
    public void render(Canvas canvas) {
        try{        	            
            chart.render(canvas);         
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }			

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub		
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);		
		return lst;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 try {          
         	chartAnimation();         	
         }
         catch(Exception e) {
             Thread.currentThread().interrupt();
         }        
	}
	
	private void chartAnimation()
	{
		  try {                            	           		
			chart.getDataAxis().hide();
			chart.getPlotLegend().hide();
          
          	int [] ltrb = getBarLnDefaultSpadding();          	
          	for(int i=8; i> 0 ;i--)
          	{
          		Thread.sleep(100);
          		chart.setPadding(ltrb[0],i *  ltrb[1], ltrb[2], ltrb[3]);	    	
          		
          		if(1 == i)
          		{          			
          			drawTitle();
          			drawDyLegend();
          		}
          		postInvalidate();    
          	} 
          	                 
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }            
	}
	
	private void drawTitle()
	{		
		//标题
		chart.setTitle("主要数据库分布情况");
		chart.addSubtitle("(XCL-Charts Demo)");	
		chart.getPlotTitle().getTitlePaint().setColor(Color.BLUE);
		chart.getPlotTitle().getSubtitlePaint().setColor(Color.BLUE);
		
		//激活点击监听
		chart.ActiveListenItemClick();
		chart.showClikedFocus();
		
		//禁用平移模式
		//chart.disablePanMode();
		//限制只能左右滑动
		chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);	
		
		
		//禁用双指缩放
		//chart.disableScale();
		
		/*
		//显示十字交叉线
		chart.showDyLine();
		DyLine dyl = chart.getDyLine();
		if( null != dyl)
		{
			dyl.setDyLineStyle(XEnum.DyLineStyle.Horizontal);
			dyl.setLineDrawStyle(XEnum.LineStyle.DASH);
		}
		*/
		
		chart.getDataAxis().show();		 
		chart.getPlotLegend().show();				
	}
	
	private void drawDyLegend()
	{
		
		Legend dyLegend = chart.getDyLegend();		
		if(null == dyLegend) return;
		dyLegend.setPosition(0.7f,0.3f);
		dyLegend.setColSpan(30.f);
		dyLegend.getBackgroundPaint().setColor(Color.BLACK);
		dyLegend.getBackgroundPaint().setAlpha(100);
		dyLegend.setRowSpan(20.f);
		dyLegend.setMargin(15.f);
		dyLegend.setStyle(XEnum.DyInfoStyle.ROUNDRECT);
		
		Paint pDyLegend = new Paint(Paint.ANTI_ALIAS_FLAG);
		pDyLegend.setColor(Color.GREEN);			
		PlotDot dotDyLegend = new PlotDot();
		dotDyLegend.setDotStyle(XEnum.DotStyle.RECT);		
		dyLegend.addLegend(dotDyLegend, "动态图例一", pDyLegend);
		
		Paint pDyLegend2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		pDyLegend2.setColor(Color.RED);
		dyLegend.addLegend(dotDyLegend, "动态图例二", pDyLegend2);
		
		Paint pDyLegend3 = new Paint(Paint.ANTI_ALIAS_FLAG);
		pDyLegend3.setColor(Color.CYAN);	
		dyLegend.addLegend(dotDyLegend,"动态图例三", pDyLegend3);
		
		Paint pDyLegend4 = new Paint(Paint.ANTI_ALIAS_FLAG);
		pDyLegend4.setColor(Color.YELLOW);
		dyLegend.addLegend(dotDyLegend,"动态图例四", pDyLegend4);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{			
			triggerClick(event.getX(),event.getY());			
		}
		return true;
	}
	
	//触发监听
	private void triggerClick(float x,float y)
	{		
		
		//交叉线
		if(chart.getDyLineVisible())chart.getDyLine().setCurrentXY(x,y);	
		
		if(!chart.getListenItemClickStatus())
		{
			//交叉线
			if(chart.getDyLineVisible()&&chart.getDyLine().isInvalidate())this.invalidate();
		}else{							
			BarPosition record = chart.getPositionRecord(x,y);			
			if( null == record)
			{
				if(chart.getDyLineVisible())this.invalidate();
				return;
			}
			
			if(record.getDataID() >= chartData.size()) return;
			BarData bData = chartData.get(record.getDataID());	
			
			if(record.getDataChildID() >= bData.getDataSet().size())return;
			Double bValue = bData.getDataSet().get(record.getDataChildID());			
	
			//显示选中框
			chart.showFocusRectF(record.getRectF());		
			chart.getFocusPaint().setStyle(Style.STROKE);
			chart.getFocusPaint().setStrokeWidth(3);		
			chart.getFocusPaint().setColor(Color.GREEN);							
			
			//在点击处显示tooltip
			mPaintToolTip.setColor(Color.RED);		
			mDotToolTip.setDotStyle(XEnum.DotStyle.RECT);		
			chart.getToolTip().setCurrentXY(x,y);
			chart.getToolTip().setStyle(XEnum.DyInfoStyle.ROUNDRECT);		
			chart.getToolTip().addToolTip(mDotToolTip, bData.getKey(), mPaintToolTip);
			chart.getToolTip().addToolTip(
						" Current Value:" +Double.toString(bValue),mPaintToolTip);
			chart.getToolTip().getBackgroundPaint().setAlpha(100);
			this.invalidate();
		}
		
	}
	

	
}
