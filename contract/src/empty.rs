#![no_std]

multiversx_sc::imports!();
multiversx_sc::derive_imports!();

#[derive(TopEncode, TopDecode, TypeAbi, Debug, PartialEq, Eq)]
pub struct LockedLpAttributes {
    pub attr1: u64,
    pub attr2: usize,
    pub attr3: u64,
}

/// An empty contract. To be used as a template when starting a new contract from scratch.
#[multiversx_sc::contract]
pub trait UtgsContract:
    multiversx_sc_modules::default_issue_callbacks::DefaultIssueCallbacksModule {
    #[init]
    fn init(&self) {}

    #[only_owner]
    #[endpoint]
    #[payable("EGLD")]
    fn issue_nft(&self, token_display_name: ManagedBuffer, token_ticker: ManagedBuffer) {
        let payment_amount = self.call_value().egld_value();
        self.participant_token_name(token_display_name.clone())
            .set(token_display_name.clone());
        self.participant_token(token_display_name.clone()).issue_and_set_all_roles(
            EsdtTokenType::NonFungible,
            payment_amount.clone_value(),
            token_display_name,
            token_ticker,
            0,
            None,
        );
    }

    #[endpoint]
    #[payable("*")]
    fn mint_nft(&self, token_name : ManagedBuffer) {
        let _caller = self.blockchain().get_caller();
        let _current_block = self.blockchain().get_block_epoch();

        self.token_id_event(self.participant_token(token_name.clone()).get_token_id());
        let staked_lp_payment = self.mint_lp_token_direct(
            token_name.clone(),
            &self.participant_token(token_name.clone()).get_token_id(),
            10usize,
            &BigUint::from(1u32),
            self.blockchain().get_block_epoch(),
            10u64,
        );
        let attr:LockedLpAttributes = self.participant_token(token_name.clone()).get_token_attributes(staked_lp_payment.token_nonce);
        self.debug_minted_event(attr.attr1, attr.attr2, attr.attr3);


        self.send().direct_esdt(
            &self.blockchain().get_caller(),
            &staked_lp_payment.token_identifier,
            staked_lp_payment.token_nonce,
            &staked_lp_payment.amount,
        );
        self.debug_minted_event(attr.attr1, attr.attr2, attr.attr3);
    }

    fn mint_lp_token_direct(
        &self,
        token_name: ManagedBuffer,
        token_id: &TokenIdentifier,
        token_type_id: usize,
        amount_to_mint: &BigUint,
        attr1: u64,
        attr3: u64,
    ) -> EsdtTokenPayment {
        let lp_token_attributes = LockedLpAttributes {
            attr1,
            attr2: token_type_id,
            attr3,
        };
        let token_display_name = token_name;
        let minted_nonce = self.send().esdt_nft_create_compact_named(
            token_id,
            amount_to_mint,
            &token_display_name,
            &lp_token_attributes,
        );
        // self.increase_aggregated_exotic_lps_amount(&token_type_id, amount_to_mint);
        EsdtTokenPayment::new(token_id.clone(), minted_nonce, amount_to_mint.clone())
    }

    #[event("debug_minted_event")]
    fn debug_minted_event(
        &self,
        #[indexed] stake_epoch: u64,
        #[indexed] locked_lp_type_id: usize,
        #[indexed] unbonding_start_epoch: u64,
    );

    #[event("token_id_event")]
    fn token_id_event(&self, #[indexed] token_id: TokenIdentifier);


    #[view(participantToken)]
    #[storage_mapper("participantToken")]
    fn participant_token(&self, token_name: ManagedBuffer) -> NonFungibleTokenMapper;

    #[view(participantTokenName)]
    #[storage_mapper("participantTokenName")]
    fn participant_token_name(&self, token_name: ManagedBuffer) -> SingleValueMapper<ManagedBuffer>;
}
