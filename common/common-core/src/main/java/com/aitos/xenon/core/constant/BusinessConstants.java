package com.aitos.xenon.core.constant;


public interface BusinessConstants
{
    /**
     * 基金会
     */
    interface ArgonFoundation{
        String publicKey="4d7e10c17f92fb1cfbb2a8268c42198bfc632e973e465531ba68194a277f9247";
        String privateKey="946b840beca0d06950b64438f86cf5c24ce714321b535ea910db2dd97e828d8e";

        String web3PublicKey="0xD909b78898AE965C90bb57056da4B18a00582d0E";
        String web3PrivateKey="fcaf466fe5337fb6259245ed23076592d560636924e2f3706ca855f0234d559a";
    }

    /**
     * 设备类型
     */
    interface DeviceMinerType{
        int VIRTUAL_MINER=1;
        int API_MINER=2;
        int DTU_MINER=3;
        int LIGHT_SOLAR_MINER=4;
    }

    interface AccountType{
        /**
         * owner账户
         */
        int OWNER=1;
        /**
         * miner账户
         */
        int MINER=2;
        /**
         * miner账户
         */
        int FOUNDATION=3;
    }

    /**
     * 交易类型
     */
    interface TXType{
        /**
         * TX_Register_Miner
         */
        int TX_REGISTER_MINER=1;

        /**
         * TX_Onboard_Miner
         */
        int TX_ONBOARD_MINER=2;

        /**
         * TX_Transfer_Miner
         */
        int TX_TRANSFER_MINER=3;

        /**
         * TX_Terminate_Miner
         */
        int TX_TERMINATE_MINER=4;

        /**
         * TX_Challenge_PoGG
         */
        int TX_CHALLENGE_POGG=5;

        /**
         * TX_Response_PoGG
         */
        int TX_RESPONSE_POGG=6;

        /**
         * TX_Reward_PoGG
         */
        int TX_REWARD_POGG=7;

    }
}
