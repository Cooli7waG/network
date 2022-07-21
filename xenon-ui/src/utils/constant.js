const Constant={
    /**
     * 交易类型
     */
    TXType: {
        /**
         * TX_Register_Miner
         */
        1: "TX_REGISTER_MINER",

        /**
         * TX_Onboard_Miner
         */
        2: "TX_ONBOARD_MINER",

        /**
         * TX_Transfer_Miner
         */
        3: "TX_TRANSFER_MINER",

        /**
         * TX_Terminate_Miner
         */
        4: "TX_TERMINATE_MINER",

        /**
         * TX_Challenge_PoGG
         */
        5: "TX_CHALLENGE_POGG",

        /**
         * TX_Response_PoGG
         */
        6: "TX_RESPONSE_POGG",

        /**
         * TX_Reward_PoGG
         */
        7: "TX_REWARD_POGG"
    },
    /**
     * 交易类型
     */
    MinerType: {
        1: "Virtual Miner",
        2: "API Miner",
        3: "DTU Miner",
        4: "Light Solar Miner"
    },
    /**
     * 账户类型
     */
    AccountType: {
        1: "Owner账户",
        2: "Miner账户",
        3: "基金会账户"
    }
}
export default Constant;
