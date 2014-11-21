package nano;


/*****************************************************************
 * @description : Biz Exception 처리하기위한 class
 * $Id: BizException.java,v 1.2 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/

public class BizException extends Exception{
	private static final long serialVersionUID = 1L;

	private String code    = null;
	private String message = null;

	public BizException ()
	{
		super();
	}

	public BizException(String code, String message)
	{
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() 
	{
		return this.code;
	}	

	public String getMessage() 
	{
		return this.message;
	}
}
