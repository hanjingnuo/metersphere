package io.metersphere.api.dto.definition.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.metersphere.api.dto.definition.request.assertions.MsAssertions;
import io.metersphere.api.dto.definition.request.auth.MsAuthManager;
import io.metersphere.api.dto.definition.request.configurations.MsHeaderManager;
import io.metersphere.api.dto.definition.request.controller.MsIfController;
import io.metersphere.api.dto.definition.request.extract.MsExtract;
import io.metersphere.api.dto.definition.request.processors.post.MsJSR223PostProcessor;
import io.metersphere.api.dto.definition.request.processors.pre.MsJSR223PreProcessor;
import io.metersphere.api.dto.definition.request.sampler.MsDubboSampler;
import io.metersphere.api.dto.definition.request.sampler.MsHTTPSamplerProxy;
import io.metersphere.api.dto.definition.request.sampler.MsJDBCSampler;
import io.metersphere.api.dto.definition.request.sampler.MsTCPSampler;
import io.metersphere.api.dto.definition.request.timer.MsConstantTimer;
import io.metersphere.api.dto.scenario.environment.EnvironmentConfig;
import io.metersphere.commons.utils.LogUtil;
import lombok.Data;
import org.apache.jmeter.protocol.http.control.AuthManager;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MsHTTPSamplerProxy.class, name = "HTTPSamplerProxy"),
        @JsonSubTypes.Type(value = MsHeaderManager.class, name = "HeaderManager"),
        @JsonSubTypes.Type(value = MsJSR223PostProcessor.class, name = "JSR223PostProcessor"),
        @JsonSubTypes.Type(value = MsJSR223PreProcessor.class, name = "JSR223PreProcessor"),
        @JsonSubTypes.Type(value = MsTestPlan.class, name = "TestPlan"),
        @JsonSubTypes.Type(value = MsThreadGroup.class, name = "ThreadGroup"),
        @JsonSubTypes.Type(value = MsAuthManager.class, name = "AuthManager"),
        @JsonSubTypes.Type(value = MsAssertions.class, name = "Assertions"),
        @JsonSubTypes.Type(value = MsExtract.class, name = "Extract"),
        @JsonSubTypes.Type(value = MsTCPSampler.class, name = "TCPSampler"),
        @JsonSubTypes.Type(value = MsDubboSampler.class, name = "DubboSampler"),
        @JsonSubTypes.Type(value = MsJDBCSampler.class, name = "JDBCSampler"),
        @JsonSubTypes.Type(value = MsConstantTimer.class, name = "ConstantTimer"),
        @JsonSubTypes.Type(value = MsIfController.class, name = "IfController"),
        @JsonSubTypes.Type(value = MsScenario.class, name = "scenario"),

})
@JSONType(seeAlso = {MsHTTPSamplerProxy.class, MsHeaderManager.class, MsJSR223PostProcessor.class,
        MsJSR223PreProcessor.class, MsTestPlan.class, MsThreadGroup.class, AuthManager.class, MsAssertions.class,
        MsExtract.class, MsTCPSampler.class, MsDubboSampler.class, MsJDBCSampler.class, MsConstantTimer.class, MsIfController.class, MsScenario.class}, typeKey = "type")
@Data
public class MsTestElement {
    private String type;
    @JSONField(ordinal = 1)
    private String id;
    @JSONField(ordinal = 2)
    private String name;
    @JSONField(ordinal = 3)
    private String label;
    @JSONField(ordinal = 4)
    private LinkedList<MsTestElement> hashTree;

    // 公共环境逐层传递，如果自身有环境 以自身引用环境为准否则以公共环境作为请求环境
    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, EnvironmentConfig config) {
        for (MsTestElement el : hashTree) {
            el.toHashTree(tree, el.hashTree, config);
        }
    }

    /**
     * 转换JMX
     *
     * @param hashTree
     * @return
     */
    public String getJmx(HashTree hashTree) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SaveService.saveTree(hashTree, baos);
            System.out.print(baos.toString());
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.warn("HashTree error, can't log jmx content");
        }
        return null;
    }

    public HashTree generateHashTree(EnvironmentConfig config) {
        HashTree jmeterTestPlanHashTree = new ListedHashTree();
        this.toHashTree(jmeterTestPlanHashTree, this.hashTree, config);
        return jmeterTestPlanHashTree;
    }

    public HashTree generateHashTree() {
        HashTree jmeterTestPlanHashTree = new ListedHashTree();
        this.toHashTree(jmeterTestPlanHashTree, this.hashTree, null);
        return jmeterTestPlanHashTree;
    }

}





