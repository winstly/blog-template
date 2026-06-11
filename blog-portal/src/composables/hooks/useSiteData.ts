import { ref, readonly } from 'vue';
import type { Profile, NavItem, SocialLink } from '@/api/types';
import { siteService } from '@/api/services';

const profile = ref<Profile | null>(null);
const navItems = ref<NavItem[]>([]);
const socialLinks = ref<SocialLink[]>([]);
const profileLoading = ref(false);
const navLoading = ref(false);
let profileFetched = false;
let navFetched = false;

export function useSiteData() {
  async function fetchProfile() {
    if (profileFetched) return;
    profileLoading.value = true;
    try {
      profile.value = await siteService.getProfile();
      profileFetched = true;
    } catch (e) {
      console.error('Failed to fetch profile:', e);
    } finally {
      profileLoading.value = false;
    }
  }

  async function fetchNavItems() {
    if (navFetched) return;
    navLoading.value = true;
    try {
      navItems.value = await siteService.getNavItems();
      navFetched = true;
    } catch (e) {
      console.error('Failed to fetch nav items:', e);
    } finally {
      navLoading.value = false;
    }
  }

  async function fetchSocialLinks() {
    if (socialLinks.value.length > 0) return;
    try {
      socialLinks.value = await siteService.getSocialLinks();
    } catch (e) {
      console.error('Failed to fetch social links:', e);
    }
  }

  return {
    profile: readonly(profile),
    navItems: readonly(navItems),
    socialLinks: readonly(socialLinks),
    profileLoading: readonly(profileLoading),
    navLoading: readonly(navLoading),
    fetchProfile,
    fetchNavItems,
    fetchSocialLinks,
  };
}
