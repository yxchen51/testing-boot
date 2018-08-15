package com.open.qa.domain;

/**
 * 测试环境列表
 * 可用户不通测试环境用不同的测试网关测试的情况
 */
public enum TestingEnvironment {
    DEV(1,"开发环境","http://biz-gateway.dev.xxx.com"),
    TEST(2,"测试环境","http://biz-gateway.test.xxx.com");


    private int index;

    private String name;

    private String domain;


    TestingEnvironment(Integer index,String name,String domain){
        this.index = index;
        this.name = name;
        this.domain =domain;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
