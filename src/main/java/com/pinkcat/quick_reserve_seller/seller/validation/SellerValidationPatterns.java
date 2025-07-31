package com.pinkcat.quick_reserve_seller.seller.validation;

public class SellerValidationPatterns {
  private SellerValidationPatterns() {}

  /**
   * ID
   *
   * <ul>
   *   <li>영문 소문자, 숫자, 밑줄(_)만 허용
   *   <li>4~20자
   *   <li>공백/대문자/특수문자 불가
   *   <li>예: user_123
   * </ul>
   */
  public static final String ID = "^[a-z0-9_]{4,20}$";

  /**
   * 이름
   *
   * <ul>
   *   <li>한글 또는 영문만 허용
   *   <li>2~20자
   *   <li>숫자/특수문자/공백 불가
   *   <li>예: 홍길동, John
   * </ul>
   */
  public static final String NAME = "^[a-zA-Z가-힣]{2,20}$";

  /**
   * 비밀번호
   *
   * <ul>
   *   <li>영문 + 숫자 필수 포함
   *   <li>특수문자 허용
   *   <li>8~20자
   *   <li>공백 불가
   *   <li>권장: 영문+숫자+특수문자 조합
   *   <li>예: abc12345, abc123!@
   * </ul>
   */
  public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]{8,20}$";

  /**
   * 휴대폰 번호 (하이픈 포함)
   *
   * <ul>
   *   <li>010-1234-5678 형식
   *   <li>02-123-4567 등 지역번호 지원
   * </ul>
   */
  public static final String PHONE_NUMBER = "^\\d{2,3}-\\d{3,4}-\\d{4}$";

  /**
   * 이메일
   *
   * <ul>
   *   <li>영문/숫자/._+- 허용 (로컬 파트)
   *   <li>@ 포함
   *   <li>도메인에 . 포함
   *   <li>예: user.name+tag@example.com
   * </ul>
   */
  public static final String EMAIL = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
}
