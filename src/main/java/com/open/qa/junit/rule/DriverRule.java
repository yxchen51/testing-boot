package com.open.qa.junit.rule;


import com.open.qa.anotations.ConditionalIgnore;
import com.open.qa.common.InitializeManager;
import com.open.qa.junit.core.IgnoreStatement;
import com.open.qa.junit.core.TestingStatement;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 重写
 * Update by liang.chen on 2020/3/01.
 */
public class DriverRule implements TestRule {

    private Map<String, String> caseParam = new HashMap<>();

    public interface IgnoreCondition {
        boolean isSatisfied();
    }

    @Override
    public Statement apply(Statement base, Description desc) {
        if (hasConditionalIgnoreAnnotation(desc)) {
            IgnoreCondition condition = getIgnoreCondition(desc, desc );
            if( condition.isSatisfied() ) {
                return new IgnoreStatement(base,desc,this);
            }
        }
        return new TestingStatement(base, desc, this);
    }


    private static boolean hasConditionalIgnoreAnnotation(Description method) {
        return method.getAnnotation(ConditionalIgnore.class) != null;
    }

    private static IgnoreCondition getIgnoreCondition(Object target, Description method ) {
        ConditionalIgnore annotation = method.getAnnotation(ConditionalIgnore.class );
        return new IgnoreConditionCreator( target, annotation ).create();
    }

    private static class IgnoreConditionCreator {
        private final Object target;
        private final Class<? extends IgnoreCondition> conditionType;

        IgnoreConditionCreator( Object target, ConditionalIgnore annotation ) {
            this.target = target;
            this.conditionType = annotation.value();
        }

        IgnoreCondition create() {
            checkConditionType();
            try {
                return createCondition();
            } catch( RuntimeException re ) {
                throw re;
            } catch( Exception e ) {
                throw new RuntimeException( e );
            }
        }

        private IgnoreCondition createCondition() throws Exception {
            IgnoreCondition result;
            if( isConditionTypeStandalone() ) {
                result = conditionType.newInstance();
            } else {
                result = conditionType.getDeclaredConstructor().newInstance();
            }
            return result;
        }

        private void checkConditionType() {
            if( !isConditionTypeStandalone() && !isConditionTypeDeclaredInTarget() ) {
                String msg
                        = "Conditional class '%s' is a member class "
                        + "but was not declared inside the test case using it.\n"
                        + "Either make this class a static class, "
                        + "standalone class (by declaring it in it's own file) "
                        + "or move it inside the test case using it";
                throw new IllegalArgumentException( String.format ( msg, conditionType.getName() ) );
            }
        }

        private boolean isConditionTypeStandalone() {
            return !conditionType.isMemberClass() || Modifier.isStatic( conditionType.getModifiers() );
        }

        private boolean isConditionTypeDeclaredInTarget() {
            return target.getClass().isAssignableFrom( conditionType.getDeclaringClass() );
        }
    }


    public Map<String, String> getCaseParam() {
        return caseParam;
    }

    public void setCaseParam(Map<String, String> caseParam) {
        this.caseParam = caseParam;
    }

    public Map<String, String> getEnvProperMap() {
        return InitializeManager.envProper;
    }

}
