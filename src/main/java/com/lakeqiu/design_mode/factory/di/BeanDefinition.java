package com.lakeqiu.design_mode.factory.di;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean定义
 * @author lakeqiu
 */
public class BeanDefinition {
    private String id;
    private String className;
    // bean属性
    private List<ConstructorArg> constructorArgs = new ArrayList<>();
    private Scope scope = Scope.SINGLETON;
    private boolean lazyInit = false;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public void addConstructorArg(ConstructorArg constructorArg) {
        this.constructorArgs.add(constructorArg);
    }

    public boolean isSingleton() {
        return scope.equals(Scope.SINGLETON);
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public String getId() {
        return id;
    }

    public List<ConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public String getClassName() {
        return className;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public static enum Scope {
        SINGLETON,
        PROTOTYPE
    }

    // bean的属性
    public static class ConstructorArg {
        // 是否是用户创建的其他bean
        private boolean isRef;
        // 属性类型
        private Class type;
        // 属性值
        private Object arg;

        private ConstructorArg(Builder builder) {
            this.isRef = builder.getIsRef();
            this.type = builder.getType();
            this.arg = builder.getArg();
        }

        public static class Builder {
            private boolean isRef = false;
            private Class type;
            private Object arg;

            public Builder setRef(Boolean isRef) {
                this.isRef = isRef;
                return this;
            }

            public Builder setType(Class type) {
                this.type = type;
                return this;
            }

            public Builder setArg(Object arg) {
                this.arg = arg;
                return this;
            }

            public ConstructorArg build() {
                if (this.isRef) {
                    if (this.type != null) {
                        throw new IllegalArgumentException("当参数为引用类型时，无需设置 type 参数");
                    }

                    // null 是 string 实例妈？
                    if (!(arg instanceof String)) {
                        throw new IllegalArgumentException("请设置引用 ID");
                    }
                } else {
                    if (this.type == null || this.arg == null) {
                        throw new IllegalArgumentException("当参数为非引用类型时，type 和 arg 参数必填");
                    }
                }

                return new ConstructorArg(this);
            }

            // Getter
            public boolean getIsRef() {
                return isRef;
            }

            public Class getType() {
                return type;
            }

            public Object getArg() {
                return arg;
            }
        }

        public boolean isRef() {
            return isRef;
        }

        public Class getType() {
            return type;
        }

        public Object getArg() {
            return arg;
        }
    }
}
