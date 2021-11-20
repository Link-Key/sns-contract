package com.peifeng.be;

import com.peifeng.be.contract.FreeEagle;
import com.peifeng.be.util.Web3JUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeApplicationTests {

    @Test
    void contextLoads() {
        FreeEagle freeEagle = Web3JUtils.getContract("","");

    }

}
