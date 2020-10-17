package inputValidation;

import java.io.IOException;
import java.io.PrintWriter; 
import java.lang.Character.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputValidationFilter implements Filter 
{
	/*steps to be taken in code (you can use the setupFilterSettings() function):
	1: Set generic field type
	2: Set field type (username, passwd, int, float...)
	3: If Numeric . set expected minimum and maximum length
	*/
	
	private String genericFieldType = "String";
	private String fieldType = "Field";
	private int expectedMinimumLength = 0;
	private int expectedMaximumLength = 50;
	
	//string patterns, expectedMaximumLength en expectedMaximumLength gebruiken hierin!!!
	private final String USERNAME_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
	private final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
	private final String SEARCHBOX_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
	private final String GENERIC_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
	
	//numeric patterns
	private final String INTEGER_PATTERN = "^[1-9]\\d*$";
	private final String FLOAT_PATTERN = "^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$";
	//email pattern not needed
	
	public InputValidationFilter()
	{
		this.genericFieldType = "String";
		this.fieldType = "Field";
		this.expectedMinimumLength = 0;
		this.expectedMaximumLength = 50;
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {}  
      
	public void doFilter(String field, RequestDispatcher rd, ServletRequest req, ServletResponse resp/*,  FilterChain chain*/) throws Exception, IOException, ServletException 
	{   //filterchain ???
		PrintWriter out = resp.getWriter();  
		out.print("filter is invoked before");  
		try
		{
			validateField(field, rd, req, resp);
			//chain.doFilter(req, resp);//sends request to next resource 
			out.print("filter is invoked after"); 
		}
		
		catch(Exception e)
		{
			//loggen 
		}  
    }  
	
	public void destroy() {} 
	
	//_________________________________________________________________________________________________________
	// getters and setters
	
	public void setGenericFieldType(String gft)
	{
		this.genericFieldType = gft;
	}
	
	public String getGenericFieldType()
	{
		return this.genericFieldType;
	}
	
	public void setFieldType(String ft)
	{
		this.fieldType = ft;
	}
	
	public String getFieldType()
	{
		return this.fieldType;
	}
	
	public void setexpectedMinimumLength(int eminl)
	{
		this.expectedMinimumLength = eminl;
	}
	
	public int getexpectedMinimumLength()
	{
		return this.expectedMinimumLength;
	}
	
	public void setexpectedMaximumLength(int emaxl)
	{
		this.expectedMaximumLength = emaxl;
	}
	
	public int getexpectedMaximumLength()
	{
		return this.expectedMaximumLength;
	}
	
	//_________________________________________________________________________________________________________
	//other functions
	
	//gft is numeric
	public void setFilterSettings(String gft, String ft, int eminl, int emaxl)
	{
		this.genericFieldType = gft;
		this.fieldType = ft;
		this.expectedMinimumLength = eminl;
		this.expectedMaximumLength = emaxl;
	}
	
	public boolean isNumeric(String str)
	{
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c)) 
			{
				return false;
			}
		}
		return true;
	}
	
	public void showErrorMessage(String errMsg, RequestDispatcher rd, ServletRequest req, ServletResponse resp)
	{
		req.setAttribute("msg", errMsg);
            try {
                rd.forward(req, resp);
            } catch (ServletException ex) {
                Logger.getLogger(InputValidationFilter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InputValidationFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
		return;
	}
	
	public void isFieldEmpty(String field, RequestDispatcher rd, ServletRequest req, ServletResponse resp)
	{
		if (field == null || "".equals(field)) 
		{
			showErrorMessage("Field(s) are empty", rd, req, resp);
		} 
	}
	
	public void validateField(String field, RequestDispatcher rd, ServletRequest req, ServletResponse resp)
	{
		Pattern pattern = null;
		Matcher matcher;
		
		if(this.genericFieldType == "String")
		{
			if(this.fieldType == "Username") 		
			{ 
				pattern = Pattern.compile(USERNAME_PATTERN); 
			}
			
			else if(this.fieldType == "Password") 	
			{ 
				pattern = Pattern.compile(PASSWORD_PATTERN); 
			}
			
			else if(this.fieldType == "Search box input") 	
			{ 
				pattern = Pattern.compile(SEARCHBOX_PATTERN); 
			}

			else if(this.fieldType == "Email")
			{
				boolean validEmail = EmailValidator.getInstance().isValid(field);
				isFieldEmpty(field, rd, req, resp);
				if(!validEmail)
				{
					//loggen in de database
					showErrorMessage(this.fieldType + " not valid!", rd, req, resp); //break doet hij in de functie zelf
				}
			}
			
			else 
			{
				//loggen of melding genereeren voor de developer dat geen field type is gespecificeerd in de code
				pattern = Pattern.compile(GENERIC_PATTERN);
			}
		}
		
		else if(genericFieldType == "Numeric")
		{
			if(this.fieldType == "Integer")
			{
				pattern = Pattern.compile(INTEGER_PATTERN); 
			}
			
			else if(this.fieldType == "Float")
			{
				pattern = Pattern.compile(FLOAT_PATTERN); 
			}
		}
		//valideer
		isFieldEmpty(field, rd, req, resp);
		matcher = pattern.matcher(field);
		if(this.genericFieldType == "String")
		{
			if(matcher.matches() == false && (this.fieldType == "Username" || this.fieldType == "Password")) 
			{
				//loggen in de database
				showErrorMessage(this.fieldType + " not valid!", rd, req, resp);
			}

			else if(matcher.matches() == false && this.fieldType == "Search box input")
			{
				//alleen loggen, hiervan wil je geen foutmelding genereren, ook geen generieke
			}
			
			else if(matcher.matches() == false && this.fieldType == "Field")
			{
				//hier is GEEN fieldtype gedefinieerd in de code en de GENERIC_PATTERN scan heeft wat gevonden, loggen in database
			}
		}
		
		else if(this.genericFieldType == "Numeric")
		{
			if(isNumeric(field))
			{
				if(matcher.matches() == false && (this.fieldType == "Integer" || this.fieldType == "Float")) 
				{
					//loggen in de database
					showErrorMessage("Number not valid!", rd, req, resp);
				}
				
				if(String.valueOf(field).length() < this.expectedMinimumLength || String.valueOf(field).length() > this.expectedMaximumLength)
				{
					//loggen in de database
					showErrorMessage("Number not valid!", rd, req, resp);
				}
			}
			
			else
			{
				//loggen in de database
				showErrorMessage("Number not valid!", rd, req, resp);
			}
		}
		
		else 
		{
			//loggen want dit is gevaarlijk denk ik
		}
	}

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}