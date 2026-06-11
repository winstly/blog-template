import { http } from '@/api/client';
import type { Profile, FriendLink, NavItem, SocialLink } from '@/api/types';

export const siteService = {
  async getProfile(): Promise<Profile> {
    return http.get('/v1/profile');
  },
  async getFriendLinks(): Promise<FriendLink[]> {
    return http.get('/v1/friend-links');
  },
  async getNavItems(): Promise<NavItem[]> {
    return http.get('/v1/nav-items');
  },
  async getSocialLinks(): Promise<SocialLink[]> {
    return http.get('/v1/social-links');
  },
};
