package cn.tomsnail.framwork.validator;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.tomsnail.framwork.validator.annotation.FieldValidator;
import cn.tomsnail.framwork.validator.annotation.TestBean;
import cn.tomsnail.framwork.validator.exception.ParamValidatorException;

  
   /**
	*        转换为bean对象
	* @author yangsong
	* @version 0.0.1
	* @status 正常
	* @date 2017年4月17日 上午9:25:59
	* @see 
	*/     
public class BeanValidator {

	
	public Object getValidBean(Map<String, Object> valueMap,Class clazz) throws ParamValidatorException{
		return this.getValidBean(valueMap, clazz, null);
		
	}
	
	public Object getValidBean(Map<String, Object> valueMap,Class clazz,String validType) throws ParamValidatorException{
		if(valueMap==null||clazz==null){
			throw new ParamValidatorException(ValidateUtil.getValidFaildMsg("", "Map值或者Key值空异常"));
		}
		boolean isAllValidator = false;
		StringBuffer sb = new StringBuffer();
		boolean isHasError = false;
		if(!clazz.isAnnotationPresent(cn.tomsnail.framwork.validator.annotation.BeanValidator.class)){
			
		}else{
			cn.tomsnail.framwork.validator.annotation.BeanValidator bv = (cn.tomsnail.framwork.validator.annotation.BeanValidator) clazz.getAnnotation(cn.tomsnail.framwork.validator.annotation.BeanValidator.class);
			isAllValidator = bv.isAllValidator();
		}
		List<Field> fs = new ArrayList<Field>();
		Field[] fs0 = clazz.getDeclaredFields();
		for(Field f:fs0){
			fs.add(f);
		}
		if(clazz.getSuperclass()!=null){
			Field[] fs1 = clazz.getSuperclass().getDeclaredFields();
			for(Field f:fs1){
				fs.add(f);
			}
			if(clazz.getSuperclass().getSuperclass()!=null){
				Field[] fs2 = clazz.getSuperclass().getSuperclass().getDeclaredFields();
				for(Field f:fs2){
					fs.add(f);
				}
			}
		}
		
		if(fs!=null&&fs.size()>0){
			Object obj = null;
			try {
				obj = clazz.newInstance();
				for(Field f:fs){
					f.setAccessible(true);
					String fieldName = f.getName();
					Class fcalzz = f.getClass();
					Object value= null;
					FieldValidator fv = null;
					if(f.isAnnotationPresent(FieldValidator.class)){
						fv = f.getAnnotation(FieldValidator.class);
						if(!StringUtils.isBlank(fv.mapperName())){
							value = valueMap.get(fv.mapperName());
						}
					}else{
						continue;
					}
					if(value==null){
						value = valueMap.get(fieldName);
					}
					String fclazzName = f.getType().getName();
					if(fclazzName.equals("java.lang.Float")||fclazzName.equals("float")){
						f.set(obj, Float.valueOf(value+""));
					}
					if(fclazzName.equals("java.lang.Double")||fclazzName.equals("double")){
						f.set(obj, Double.valueOf(value+""));
					}
					if(fclazzName.equals("java.util.Date")){
						String dateFormat = fv.dateFormat();
						if(StringUtils.isBlank(dateFormat)){
							dateFormat = "yyyy-MM-dd HH:mm:ss";
						}
						try {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
							f.set(obj, simpleDateFormat.parseObject(value+""));
						} catch (Exception e) {
							if(isAllValidator){
								sb.append(ValidateUtil.getValidFaildMsg(fv.errorMsg().length>0?fv.errorMsg()[0]:"", fieldName+"转换日期错误")).append(";");
								isHasError = true;
							}else{
								throw new ParamValidatorException(ValidateUtil.getValidFaildMsg(fv.errorMsg().length>0?fv.errorMsg()[0]:"", fieldName+"转换日期错误"));
							}
						}
					}
					Object _value = null;
					if(fv!=null&&!fv.onlyToBean()){
						String[] validTypes = fv.validTypes();
						boolean isDoValid = false;
						if((validTypes==null||validTypes.length==0)&&validType==null){
							isDoValid = true;
						}else{
							for(int i=0;i<validTypes.length;i++){
								if(validTypes[i].equals(validType)){
									isDoValid = true;
									break;
								}
							}
						}
						if(isDoValid){
							RuleType[] rules = fv.rules();
							String[] values = fv.values();
							AValidator v = null;
							if(fclazzName.equals("java.lang.String")){
								v = ValidatorFactory.getStringValidator();
							}
							if(fclazzName.equals("java.lang.Long")||fclazzName.equals("long")){
								v = ValidatorFactory.getLongValidator();
							}
							if(fclazzName.equals("java.lang.Integer")||fclazzName.equals("int")){
								v = ValidatorFactory.getIntegerValidator();
							}
							if(rules==null||values==null||rules.length!=values.length){
								_value = value;
							}else{
								Map<RuleType,String> errorMap = new HashMap<RuleType, String>();
								String[] es = fv.errorMsg();
								for(int i=0;i<rules.length;i++){
									if(StringUtils.isBlank(values[i])){
										v.add(rules[i]);
									}else{
										v.add(rules[i], values[i]);
									}
									if(i<es.length){
										errorMap.put(rules[i], es[i]);
									}else{
										errorMap.put(rules[i], "");
									}
								}
								if(isAllValidator){
									try {
										_value = v.getBeanValidatorValue(fieldName, value, errorMap);
									} catch (ParamValidatorException e) {
										sb.append(e.getMessage()).append(";");
										isHasError = true;
									}
								}else{
									_value = v.getBeanValidatorValue(fieldName, value, errorMap);
								}
							}
						}else{
							_value = value;
						}
					}else{
						_value = value;
					}
					if(!isHasError){
						if(!"null".equals(_value)&&_value!=null){
							if(fclazzName.equals("java.lang.String")){
								f.set(obj, String.valueOf(_value+""));
								
							}
							if(fclazzName.equals("java.lang.Long")||fclazzName.equals("long")){
								f.set(obj, Long.valueOf(_value+""));
							}
							if(fclazzName.equals("java.lang.Integer")||fclazzName.equals("int")){
								f.set(obj, Integer.valueOf(_value+""));
							}
						}
						
					}
				}
				if(isHasError){
					throw new ParamValidatorException(sb.toString());
				}
				return obj;
			} catch (ParamValidatorException e) {
				throw e;
			}catch (Exception e) {
				e.printStackTrace();
				throw new ParamValidatorException(ValidateUtil.getValidFaildMsg("", "初始化参数对象错误"));
			}
		}else{
			throw new ParamValidatorException(ValidateUtil.getValidFaildMsg("", "bean对象属性不可读"));
		}
	}
	
	public static void main(String[] args) {
		Map<String,Object> t = new HashMap<String,Object>();
		t.put("email", "123@xx.com");
	
		try {
			long d1 = System.currentTimeMillis();
			for(int i=0;i<1;i++){
				TestBean testBean = (TestBean) ValidatorFactory.getBeanValidator().getValidBean(t, TestBean.class);
				System.out.println(testBean.getPhone());
			}
			System.out.println(System.currentTimeMillis()-d1);
		} catch (ParamValidatorException e) {
			e.printStackTrace();
		}
	}
	
}
