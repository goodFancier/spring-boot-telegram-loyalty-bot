package com.springboottelegrambot.model.enums;

public enum GenderEnum
{
		Male("Мужчина"), Female("Женщина");

		GenderEnum(String title)
		{
				this.title = title;
		}

		private String title;

		public static GenderEnum getByInt(Integer value)
		{
				if(value == null)
						return null;
				for(GenderEnum iterable_element : values())
						if(value.equals(iterable_element.ordinal()))
								return iterable_element;
				return null;
		}

		public static GenderEnum getByBoolean(Boolean value)
		{
				return value? GenderEnum.Male: Female;
		}

		public static GenderEnum getByString(String gender)
		{
				if(gender == null)
						return null;
				for(GenderEnum element : GenderEnum.values())
						if(element.getTitle().equalsIgnoreCase(gender))
								return element;
				return null;
		}

		public static GenderEnum getBySexType(String sexTypeName)
		{
				switch(sexTypeName)
				{
						case "MALE":
								return GenderEnum.Male;
						case "FEMALE":
								return GenderEnum.Female;
						default:
								return null;
				}
		}

		public String intValue()
		{
				switch(this)
				{
						case Male:
								return "1";
						case Female:
								return "0";
						default:
								return null;
				}
		}

		@Override
		public String toString()
		{
				return getTitle();
		}

		public String getTitle()
		{
				return title;
		}

		public void setTitle(String title)
		{
				this.title = title;
		}
}
