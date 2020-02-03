library("tidyverse")
library("ggplot2")

print(data)
ggplot(data=data, aes(x=Size, y=Time, group=Type)) 
  geom_line(aes(color=Type))+
  geom_point(aes(color=Type))

  p<-ggplot(data, aes(x=Size, y=Time, group=Type)) +
    geom_line(aes(color=Type))+
    geom_point(aes(color=Type))
  p

  filteredData<-filter(data, RunOrder == "onStart")
  view(filteredData)
  f<-ggplot(filteredData, aes(x=Size, y=Time, group=Type)) +
    geom_line(aes(color=Type))+
    geom_point(aes(color=Type))
  f  
  
  fd<-filter(filteredData, Type != "distributed")
  
  fa<-ggplot(fd, aes(x=Size, y=Time, group=Type)) +
    geom_line(aes(color=Type))+
    
  fa  
  
  realTime<-filter(data, RunOrder !="onStart")
  
  rt<-ggplot(realTime, aes(x=Size, y=Time, group=Type)) +
    geom_line(aes(color=Type))+
    scale_x_continuous(labels=function(n){format(n, scientific = FALSE)})
  rt
  
  finalData<-filter(data, RunOrder == "onStart")
  finalMap<-ggplot(finalData, aes(x=Size, y=Time, group=Type)) +
    geom_line(aes(color=Type))+
    scale_x_continuous(labels=function(n){format(n, scientific = FALSE)})+
    labs(
         x ="Image size (pixels)", y = "Execution time (milliseconds)")
  finalMap
  
  finalData2<-filter(finalData, Time<490)
  
  finalMap<-ggplot(finalData2, aes(x=Size, y=Time, group=Type), size = 10) +
    geom_line(aes(color=Type),arrow = arrow(length=unit(0.30,"cm"), ends="last", type = "closed"))+
    scale_x_continuous(labels=function(n){format(n, scientific = FALSE)})+
    labs(
      x ="Image size (pixels)", y = "Execution time (milliseconds)")
  finalMap
  
  afterStart<-ggplot(dataNew, aes(x=Size, y=Time, group=Type), size = 10) +
    geom_line(aes(color=Type),arrow = arrow(length=unit(0.30,"cm"), ends="last", type = "closed"),size = 2)+
    scale_x_continuous(labels=function(n){format(n, scientific = FALSE)})+
    labs(
      x ="Image size (pixels)", y = "Execution time (milliseconds)")+
     theme(legend.position = c(0.73211, 0.975),
            legend.direction = "horizontal")+
    theme(axis.text.x = element_text(face="bold", color="#000000", 
                                     size=14, angle=0),
          axis.text.y = element_text(face="bold", color="#000000", 
                                     size=14, angle=0))
  
  
  afterStart
  
  
  
  
  