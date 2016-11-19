package milansluka.HealthClinicWeb.pages;

import org.apache.tapestry5.annotations.PageActivationContext;

import ehc.bo.impl.AppointmentScheduler;

public class About
{
  @PageActivationContext
  private String learn;


  public String getLearn() {
	AppointmentScheduler appointmentScheduler = null;
    return learn;
  }

  public void setLearn(String learn) {
    this.learn = learn;
  }
}
